/**
 * Handler that will be called during the execution of a PreUserRegistration flow.
 *
 * @param {Event} event - Details about the context and user that is attempting to register.
 * @param {PreUserRegistrationAPI} api - Interface whose methods can be used to change the behavior of the signup.
 */
exports.onExecutePreUserRegistration = async (event, api) => {

    const domain_mappings = {
        "uea.ac.uk": ["eafa"],
        "iwm.org.uk": ["iwm"] ,
        "filmlondon.org.uk": ["lsa"],
        "lincoln.ac.uk": ["mace"],
        "northernirelandscreen.co.uk": ["nis"] ,
        "nls.uk": ["nls"],
        "llgc.org.uk": ["nssaw"],
        "mmu.ac.uk": ["nwfa"],
        "brighton.ac.uk": ["sase"] ,
        "plymouth.gov.uk": ["thebox"],
        "hants.gov.uk": ["wfsa"],
        "yorksj.ac.uk" : ["nefa", "yfa"],
        "bfi.org.uk": ["bfi"]
    }

    const user_email = event.user.email;
    if(!user_email) {
        console.warn("User [" + event.user.email + "] does not have an email address")
        return;
    }

    const domain = user_email.split('@').pop()
    if (!domain_mappings[domain]) {
        console.warn("User [" + event.user.email + "] does not have mapped organisations")
        return;
    }

    api.user.setAppMetadata("orgs", domain_mappings[domain]);
};
