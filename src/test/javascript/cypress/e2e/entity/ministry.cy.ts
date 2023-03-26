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

describe('Ministry e2e test', () => {
  const ministryPageUrl = '/ministry';
  const ministryPageUrlPattern = new RegExp('/ministry(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ministrySample = { name: 'zero Avon', description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let ministry;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ministries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ministries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ministries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ministry) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ministries/${ministry.id}`,
      }).then(() => {
        ministry = undefined;
      });
    }
  });

  it('Ministries menu should load Ministries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ministry');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Ministry').should('exist');
    cy.url().should('match', ministryPageUrlPattern);
  });

  describe('Ministry page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ministryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Ministry page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ministry/new$'));
        cy.getEntityCreateUpdateHeading('Ministry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ministryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ministries',
          body: ministrySample,
        }).then(({ body }) => {
          ministry = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ministries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/ministries?page=0&size=20>; rel="last",<http://localhost/api/ministries?page=0&size=20>; rel="first"',
              },
              body: [ministry],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(ministryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Ministry page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ministry');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ministryPageUrlPattern);
      });

      it('edit button click should load edit Ministry page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ministry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ministryPageUrlPattern);
      });

      it('edit button click should load edit Ministry page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ministry');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ministryPageUrlPattern);
      });

      it('last delete button click should delete instance of Ministry', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ministry').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ministryPageUrlPattern);

        ministry = undefined;
      });
    });
  });

  describe('new Ministry page', () => {
    beforeEach(() => {
      cy.visit(`${ministryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Ministry');
    });

    it('should create an instance of Ministry', () => {
      cy.get(`[data-cy="name"]`).type('Mission').should('have.value', 'Mission');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ministry = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ministryPageUrlPattern);
    });
  });
});
