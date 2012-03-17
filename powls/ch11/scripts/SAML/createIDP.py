connect(sys.argv[1],sys.argv[2],sys.argv[3])

edit()
startEdit()


cd('/Servers/myserver/SingleSignOnServices/myserver')
cmo.setLoginReturnQueryParameter('returnURL')
cmo.setIdentityProviderEnabled(true)
cmo.setIdentityProviderRedirectBindingEnabled(false)
cmo.setLoginURL('/SAMLSSOWebApp/login.jsp')
cmo.setLoginReturnQueryParameter('returnURL')
cmo.setIdentityProviderPOSTBindingEnabled(false)
cmo.setIdentityProviderPreferredBinding('HTTP/Redirect')
cmo.setIdentityProviderRedirectBindingEnabled(true)
cmo.setIdentityProviderPOSTBindingEnabled(true)

activate()


