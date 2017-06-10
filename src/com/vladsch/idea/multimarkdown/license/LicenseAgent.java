/*
 * Copyright (c) 2015-2016 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package com.vladsch.idea.multimarkdown.license;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.containers.HashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.json.JsonObject;
import java.util.Map;

/**
 * Created by CrazyBunQnQ on 2017/6/10.
 */
public class LicenseAgent {
    private static final Logger logger = Logger.getInstance("com.vladsch.idea.multimarkdown.license.agent");

    // RELEASE : set to false for release
    private static final boolean LOG_AGENT_INFO = false;

    final static private String agent_signature = "475f99b03f6ec213729d7f5d577c80aa";
    final static private String license_pub = "-----BEGIN PUBLIC KEY-----\n" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAlnefMGqNu1Q9hcI2Rd8G\n" +
            "xyKlXQIFyXWIkYODRrLjvEwXYw0yksgjZeIC4g+hakyQNiN+TGE/xvo3fqB0CU4A\n" +
            "aE33Mu7jB27dt1IItcmBhJBwIhmZDc0SWNj6ywvnLeUU/NSWWbJ1SaXzPQJ2Mm5T\n" +
            "Mr3wDFhCTp80pN4svOQmdQPFSKXwdGI+n8gJvc28vRgD8As2XxgkYsZPNjefOsla\n" +
            "GHS8CNw6uI8Ijcf9hfX22twQZ+auYNL/vqtBEKq2jNLwoHTo68s+0JWJu2YILlIe\n" +
            "VQzXcXZyhhAVdZrMNGhBPiXUia6YrJpqZNDZ35CE+Y6ecs9c5AG2wpFJHnic2cjZ\n" +
            "Kh+ba83DpA3GxYa1OGMGZNaIqCjuK7A82ZPriXsoxL6YJzqSlbF/2l2x4Y3VoVTF\n" +
            "LWKEpjvLOuDOev0CH41nzkGD4Yo5CwHPZFun/WekqUBUXtxR/uH0ThoxV93exTLc\n" +
            "YwWC5GqVZfN38Ye7iDljIFVzxxP3unBy0FItg52407CZyH/gTB+Zm++fZJdKbZcl\n" +
            "UFvxtACEJvdgdM30FHuQlvS53mEXOMAzpJPVZu2gRoTl8cSO3GKxaNP9dmPCzD4a\n" +
            "gO/kVrO/c6DerwWvCJJhifKlDc6CfjM3FfWsVI2gw3WduFPJcIsLxlUqzBh95rA1\n" +
            "R+BTr2n3DV41OK5AwtCQO40CAwEAAQ==\n" +
            "-----END PUBLIC KEY-----\n";

    final static private String licenseHeader = "-----BEGIN MARKDOWN-NAVIGATOR LICENSE-----";
    final static private String licenseFooter = "-----END MARKDOWN-NAVIGATOR LICENSE-----";
    final static private String activationHeader = "-----BEGIN MARKDOWN-NAVIGATOR ACTIVATION-----";
    final static private String activationFooter = "-----END MARKDOWN-NAVIGATOR ACTIVATION-----";
    final static private String altLicenseHeader = "-----BEGIN IDEA-MULTIMARKDOWN LICENSE-----";
    final static private String altLicenseFooter = "-----END IDEA-MULTIMARKDOWN LICENSE-----";
    final static private String altActivationHeader = "-----BEGIN IDEA-MULTIMARKDOWN ACTIVATION-----";
    final static private String altActivationFooter = "-----END IDEA-MULTIMARKDOWN ACTIVATION-----";

    // RELEASE : comment out DEV LICENSE SERVER
    final static public String siteURL = "https://vladsch.com";
    final static public String authSiteURL = "auth.vladsch.com";
    final static public String auth1SiteURL = "dev.vladsch.com";
    final static public String auth2SiteURL = "vladsch.com";

    // DEBUG : debug site and licensing URLs
    //final static public String siteURL = "vladsch.dev";
    //final static public String siteURL = "https://dev.vladsch.com";
    //final static public String authSiteURL = "vladsch.dev";
    //final static public String auth1SiteURL = authSiteURL;
    //final static public String auth2SiteURL = authSiteURL;

    final static public String productPrefixURL = "/product/markdown-navigator";
    final static public String altProductPrefixURL = "/product/multimarkdown";
    final static public String patchRelease = siteURL + productPrefixURL + "/patch-release";
    final static public String eapRelease = siteURL + productPrefixURL + "/eap-release";
    final static public String altPatchRelease = siteURL + altProductPrefixURL + "/patch-release";
    final static public String altEapRelease = siteURL + altProductPrefixURL + "/eap-release";

    final static private String licenseURL = authSiteURL + productPrefixURL + "/json/license";
    final static private String alt1LicenseURL = auth1SiteURL + productPrefixURL + "/json/license";
    final static private String alt2LicenseURL = auth2SiteURL + productPrefixURL + "/json/license";
    final static public String tryPageURL = siteURL + productPrefixURL + "/try";
    final static public String buyPageURL = siteURL + productPrefixURL + "/buy";
    final static public String specialsPageURL = siteURL + productPrefixURL + "/specials";
    final static public String productPageURL = siteURL + productPrefixURL;
    final static public String referralsPageURL = siteURL + productPrefixURL + "/referrals";
    final static public String feedbackURL = auth2SiteURL + productPrefixURL + "/json/diagnostic";
    final static public String feedbackURL1 = authSiteURL + productPrefixURL + "/json/diagnostic";
    private static final String ACTIVATION_EXPIRES = "activation_expires";
    private static final String LICENSE_EXPIRES = "license_expires";
    private static final String PRODUCT_VERSION = "product_version";
    private static final String PRODUCT_RELEASED_AT = "product_released_at";
    private static final String AGENT_SIGNATURE = "agent_signature";
    private static final String LICENSE_CODE = "license_code";
    private static final String LICENSE_TYPE = "license_type";
    private static final String LICENSE_FEATURES = "license_features";
    private static final String LICENSE_FEATURE_LIST = "feature_list";
    private static final String ACTIVATION_CODE = "activation_code";
    private static final String HOST_PRODUCT = "host_product";
    private static final String HOST_NAME = "host_name";
    private static final String ACTIVATED_ON = "activated_on";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String STATUS_DISABLE = "disable";
    private static final String STATUS_OK = "ok";
    private static final String STATUS_ERROR = "error";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String LICENSE_TYPE_TRIAL = "trial";
    public static final String LICENSE_TYPE_SUBSCRIPTION = "subscription";
    public static final String LICENSE_TYPE_LICENSE = "license";

    private String license_code;
    private String activation_code;
    private JsonObject activation;
    private String license_expires;
    private String product_released_at;
    private String product_version;
    private JsonObject json; // last server response
    private boolean remove_license;
    private String license_type;
    private int license_features;
    private Map<String, Integer> featureList = new HashMap<String, Integer>();
    private String lastCommunicationsError = null;
    private boolean isSecureConnection = true;

    public Map<String, Integer> getFeatureList() {
        return featureList;
    }

    public LicenseAgent(LicenseAgent other) {
        this();
        updateFrom(other);
    }

    public boolean isSecureConnection() {
        return isSecureConnection;
    }

    public void setSecureConnection(boolean secureConnection) {
        isSecureConnection = secureConnection;
    }

    public void updateFrom(LicenseAgent other) {
        this.license_code = other.license_code;
        this.activation_code = other.activation_code;
        this.activation = other.activation;
        this.license_expires = other.license_expires;
        this.product_version = other.product_version;
        this.json = other.json;
        this.remove_license = other.remove_license;
        this.license_type = other.license_type;
        this.license_features = other.license_features;
        this.featureList = other.featureList;
        this.isSecureConnection = other.isSecureConnection;
    }

    public boolean isRemoveLicense() {
        return remove_license;
    }

    @NotNull
    public static String getTrialLicenseURL() {
        return tryPageURL;
    }

    public void setLicenseCode(String license_code) {
    }

    public void setLicenseActivationCodes(String license_code, String activation_code) {
    }

    public void setActivationCode(String activation_code) {
    }

    @NotNull
    public static String getLicenseURL() {
        return buyPageURL;
    }

    @NotNull
    public String licenseCode() {
        return license_code != null ? license_code : "";
    }

    @Nullable
    public String getLicenseExpires() {
        return "2099-12-31";//改成 2099 年才过期
    }

    @Nullable
    public String getProductVersion() {
        return product_version;
    }

    @NotNull
    public String getMessage() {
        String message;
        return json != null ? ((message = json.getString(MESSAGE)) != null ? message : "") : "";
    }

    @Nullable
    public String getStatus() {
        return json != null ? json.getString(STATUS) : null;
    }

    @NotNull
    public String activationCode() {
        return activation_code != null ? activation_code : "";
    }

    @Nullable
    public JsonObject getActivation() {
        return activation;
    }

    @Nullable
    public String getLastCommunicationsError() {
        return lastCommunicationsError;
    }

    public LicenseAgent() {
        featureList = new HashMap<>();
        featureList.put("enhanced", 1);
        featureList.put("development", 2);
    }

    //此方法会想服务器请求激活信息
    public boolean getLicenseCode(LicenseRequest licenseRequest) {
        return true;
    }

    public boolean isValidLicense() {
        return true;
    }

    public boolean isValidActivation() {
        return true;
    }

    @NotNull
    public String getLicenseType() {
        return LICENSE_TYPE_LICENSE;
    }

    public int getLicenseFeatures() {
        return LicensedFeature.Feature.LICENSE.getLicenseFlags();//其实就是 4
    }

    @NotNull
    public String getLicenseExpiration() {
        return "2099-12-31";//也改成 2099 年
    }

    @NotNull
    public String getHostName() {
        if (activation != null && activation.containsKey(HOST_NAME)) {
            return activation.getString(HOST_NAME);
        }
        return "";
    }

    @NotNull
    public String getHostProduct() {
        if (activation != null && activation.containsKey(HOST_PRODUCT)) {
            return activation.getString(HOST_PRODUCT);
        }
        return "";
    }

    @NotNull
    public String getActivatedOn() {
        return "2017-01-01";//改成在 2017 年 1 月激活的
    }

    public int getLicenseExpiringIn() {
        return Integer.MAX_VALUE;//天
    }

    public boolean getProductIsPerpetual() {
        // see if the license is valid because product is perpetually licensed
        return getLicenseExpiringIn() <= 0;
    }

    public static int floorDiv(int var0, int var1) {
        int var2 = var0 / var1;
        if ((var0 ^ var1) < 0 && var2 * var1 != var0) {
            --var2;
        }

        return var2;
    }

    public static long floorDiv(long var0, long var2) {
        long var4 = var0 / var2;
        if ((var0 ^ var2) < 0L && var4 * var2 != var0) {
            --var4;
        }

        return var4;
    }

    public boolean isActivationExpired() {
        return false;
    }
}
