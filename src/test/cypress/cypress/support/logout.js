Cypress.Commands.add('logout', (overrides = {}) => {

    Cypress.log({
        name: 'Logging out of UV and Auth0...'
    });

    // Log out of the logging application
    cy.request({
        method: 'GET',
        url: Cypress.env('UV_BASE_URL') + '/logout'
    })

    // Log out of Auth0
    cy.request({
        method: 'GET',
        url: Cypress.env('AUTH0_BASE_URL') + '/v2/logout',
        headers: {
            'Accept': 'text/html'
        }
    })
})
