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

describe('League e2e test', () => {
  const leaguePageUrl = '/league';
  const leaguePageUrlPattern = new RegExp('/league(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const leagueSample = { name: 'Indiana', description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let league;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/leagues+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/leagues').as('postEntityRequest');
    cy.intercept('DELETE', '/api/leagues/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (league) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/leagues/${league.id}`,
      }).then(() => {
        league = undefined;
      });
    }
  });

  it('Leagues menu should load Leagues page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('league');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('League').should('exist');
    cy.url().should('match', leaguePageUrlPattern);
  });

  describe('League page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaguePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create League page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/league/new$'));
        cy.getEntityCreateUpdateHeading('League');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', leaguePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/leagues',
          body: leagueSample,
        }).then(({ body }) => {
          league = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/leagues+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/leagues?page=0&size=20>; rel="last",<http://localhost/api/leagues?page=0&size=20>; rel="first"',
              },
              body: [league],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaguePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details League page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('league');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', leaguePageUrlPattern);
      });

      it('edit button click should load edit League page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('League');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', leaguePageUrlPattern);
      });

      it('edit button click should load edit League page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('League');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', leaguePageUrlPattern);
      });

      it('last delete button click should delete instance of League', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('league').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', leaguePageUrlPattern);

        league = undefined;
      });
    });
  });

  describe('new League page', () => {
    beforeEach(() => {
      cy.visit(`${leaguePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('League');
    });

    it('should create an instance of League', () => {
      cy.get(`[data-cy="name"]`).type('initiatives').should('have.value', 'initiatives');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        league = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', leaguePageUrlPattern);
    });
  });
});
