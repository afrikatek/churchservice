import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('BaptismHistory e2e test', () => {
  const baptismHistoryPageUrl = '/baptism-history';
  const baptismHistoryPageUrlPattern = new RegExp('/baptism-history(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const baptismHistorySample = { lutheran: true };

  let baptismHistory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/baptism-histories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/baptism-histories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/baptism-histories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (baptismHistory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/baptism-histories/${baptismHistory.id}`,
      }).then(() => {
        baptismHistory = undefined;
      });
    }
  });

  it('BaptismHistories menu should load BaptismHistories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('baptism-history');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BaptismHistory').should('exist');
    cy.url().should('match', baptismHistoryPageUrlPattern);
  });

  describe('BaptismHistory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(baptismHistoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BaptismHistory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/baptism-history/new$'));
        cy.getEntityCreateUpdateHeading('BaptismHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', baptismHistoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/baptism-histories',
          body: baptismHistorySample,
        }).then(({ body }) => {
          baptismHistory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/baptism-histories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/baptism-histories?page=0&size=20>; rel="last",<http://localhost/api/baptism-histories?page=0&size=20>; rel="first"',
              },
              body: [baptismHistory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(baptismHistoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BaptismHistory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('baptismHistory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', baptismHistoryPageUrlPattern);
      });

      it('edit button click should load edit BaptismHistory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BaptismHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', baptismHistoryPageUrlPattern);
      });

      it('edit button click should load edit BaptismHistory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BaptismHistory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', baptismHistoryPageUrlPattern);
      });

      it('last delete button click should delete instance of BaptismHistory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('baptismHistory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', baptismHistoryPageUrlPattern);

        baptismHistory = undefined;
      });
    });
  });

  describe('new BaptismHistory page', () => {
    beforeEach(() => {
      cy.visit(`${baptismHistoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BaptismHistory');
    });

    it('should create an instance of BaptismHistory', () => {
      cy.get(`[data-cy="lutheran"]`).should('not.be.checked');
      cy.get(`[data-cy="lutheran"]`).click().should('be.checked');

      cy.get(`[data-cy="previousParish"]`).type('TCP').should('have.value', 'TCP');

      cy.get(`[data-cy="baptised"]`).should('not.be.checked');
      cy.get(`[data-cy="baptised"]`).click().should('be.checked');

      cy.get(`[data-cy="baptismDate"]`).type('2023-03-26').blur().should('have.value', '2023-03-26');

      cy.get(`[data-cy="baptisedAt"]`).type('2023-03-25').blur().should('have.value', '2023-03-25');

      cy.get(`[data-cy="confirmed"]`).should('not.be.checked');
      cy.get(`[data-cy="confirmed"]`).click().should('be.checked');

      cy.get(`[data-cy="confirmationDate"]`).type('2023-03-26').blur().should('have.value', '2023-03-26');

      cy.get(`[data-cy="parishConfirmed"]`).type('Analyst Mexico').should('have.value', 'Analyst Mexico');

      cy.get(`[data-cy="married"]`).should('not.be.checked');
      cy.get(`[data-cy="married"]`).click().should('be.checked');

      cy.get(`[data-cy="marriageDate"]`).type('2023-03-25').blur().should('have.value', '2023-03-25');

      cy.get(`[data-cy="parishMarriedAt"]`).type('Yemeni withdrawal Cr').should('have.value', 'Yemeni withdrawal Cr');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        baptismHistory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', baptismHistoryPageUrlPattern);
    });
  });
});
