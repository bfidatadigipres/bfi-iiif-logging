Cypress.Commands.add('login', (overrides = {}) => {

    Cypress.log({
        name: 'Logging in via Auth0...',
    });

    cy.request({
        method: 'GET',
        url: Cypress.env('UV_BASE_URL'),
        headers: {
            'Accept': 'text/html'
        },
        followRedirect: false
    }).then(response => {
        cy.wrap(response.headers['location']).as('uvAuthUrl')
    })

    cy.get('@uvAuthUrl').then(uvAuthUrl => {
        cy.request({
            method: 'GET',
            url: uvAuthUrl,
            headers: {
                'Accept': 'text/html'
            },
            followRedirect: false
        }).then(response => {
            cy.wrap(response.headers['location']).as('auth0AuthUrl')
        })
    })

    cy.get('@auth0AuthUrl').then(auth0AuthUrl => {
        cy.request({
            method: 'GET',
            url: auth0AuthUrl,
            headers: {
                'Accept': 'text/html'
            },
            followRedirect: false
        }).then(response => {
            cy.wrap(response.headers['location']).as('auth0LoginPath')
        })
    })

    cy.get('@auth0LoginPath').then(auth0LoginPath => {
        cy.request({
            method: 'POST',
            url: Cypress.env('AUTH0_BASE_URL') + auth0LoginPath,
            headers: {
                'Accept': 'text/html'
            },
            body: {
                username: Cypress.env('AUTH0_USERNAME'),
                password: Cypress.env('AUTH0_PASSWORD')
            }
        })
    })
})
