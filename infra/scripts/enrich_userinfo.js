async function enrichPatronAttributes(user, context, callback) {

    const connectionHandlers = {
        'Username-Password-Connection': getUsernamePasswordAttributes
    };

    try {
        const connectionHandler = connectionHandlers[context.connection];
        if (connectionHandler) {
            const idToken = context.idToken || {};
            idToken['https://bfi.org.uk/'] = await connectionHandler(user);
            context.idToken = idToken;
        }
        callback(null, user, context);
    } catch (error) {
        callback(error);
    }

    async function getUsernamePasswordAttributes(user) {
        return user.app_metadata; // Treat all 'app_metadata' attributes as namespaced attributes
    }
}
