/// <reference types="cypress" />

describe('universal viewer', () => {

    before(() => {
        cy.logout()
        cy.login()
    })

    beforeEach(() => {
        Cypress.Cookies.preserveOnce('JSESSIONID')
    })

    it('should show the universal viewer', () => {

        cy.visit(Cypress.env('UV_BASE_URL'))
            .url().should('include', Cypress.env('UV_BASE_URL'))

        cy.get('div.uv').should('exist')
    })

    it('should navigate back and forth through the viewer', () => {

        cy.get('button[class="btn imageBtn next"]').click()
        cy.get('button[class="btn imageBtn prev"]').click()
    })
})
