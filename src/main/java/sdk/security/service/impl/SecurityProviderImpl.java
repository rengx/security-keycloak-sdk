package sdk.security.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.keycloak.representations.AccessToken;

import sdk.security.service.ISecurityProvider;
import sdk.security.util.KeycloakUtil;
import sdk.security.util.PropertiesUtil;
import sdk.security.util.RestRequestUtils;

/**
 * Keycloak 实现类
 * 
 */
public class SecurityProviderImpl implements ISecurityProvider {

    /**
     * 获取安全中心的服务根地址
     * 
     * @return String 服务根URL
     */
    public String getSecurityContextUrl(){
        return KeycloakUtil.getSecurityContextUrl();
    }

    /**
     * 获取注销url
     * 
     * @param backUrl
     * @return
     */
    public String getLogoutUrl(String backUrl){
        return KeycloakUtil.getLogoutUrl(backUrl);
    }
    
    public String getRealmInfo(){
    	return KeycloakUtil.getRealm();
    }
    
    public String getTenantRealm() {
    	return KeycloakUtil.getTenantRealm();
	}

	public Map<String, String> getTenantAdminUser() {
		String presentRealm = getTenantRealm();
		
		return getTenantAdminUser(presentRealm);
	}
	
	public Map<String, String> getTenantAdminUser(String tenantRealm) {
		
		Map<String, String> user = new HashMap<String, String>();
		/*if("tenant".equals(tenantRealm)) {
			user.put("userId", "superadmin");
			user.put("principalName", "superadmin-master");
			return user;
		}*/
		
		// TODO 不能写死9000
		String server = PropertiesUtil.getValue("conf.properties", "bigdata.domain");
		if(server!=null && server.contains(":")) {
			server = server.split(":")[0];
		}
		StringBuffer sr = new StringBuffer();
		sr.append("http://");
		sr.append(server);
		sr.append(":9000/indata-manage-portal/service/api/manage/tenants/realm/{realm}/adminuser");
		
		
		Map<String, String> uriVariables = null;
		if(tenantRealm != null && !"".equals(tenantRealm)){
			uriVariables = new HashMap<String, String>();
			uriVariables.put("realm", tenantRealm);
		}
		
		user = RestRequestUtils.get(sr.toString(), Map.class, uriVariables, null, null);
		return user;

	}
}
