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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.containers.HashMap;
import com.intellij.util.net.HttpConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.NoSuchPaddingException;
import javax.json.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
        String trimmed = license_code.trim();
        if (!trimmed.equals(this.license_code == null ? "" : this.license_code)) {
            this.license_code = trimmed;
            setActivationCode(null);
        }
    }

    public void setLicenseActivationCodes(String license_code, String activation_code) {
        String trimmed = license_code.trim();
        if (!trimmed.equals(this.license_code == null ? "" : this.license_code)) {
            this.license_code = trimmed;
            setActivationCode(activation_code);
        } else {
            setActivationCode(activation_code);
        }
    }

    public void setActivationCode(String activation_code) {
        if (activation_code != null) {
            String trimmed = activation_code.trim();
            if (!trimmed.equals(this.activation_code == null ? "" : this.activation_code)) {
                this.activation_code = trimmed;
                this.json = null;
                this.activation = null;

                if (!isValidActivation()) {
                    this.activation_code = null;
                    this.json = null;
                    this.activation = null;
                }
            }
        } else {
            this.activation_code = null;
            this.json = null;
            this.activation = null;
        }
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
        return "Never Expires";
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

    }

    public boolean getLicenseCode(LicenseRequest licenseRequest) {
        licenseRequest.agent_signature = agent_signature;
        String[] useLicenseUrls = new String[] { licenseURL, alt1LicenseURL, alt2LicenseURL };
        InputStream inputStream = null;
        String protocol = isSecureConnection ? "https://" : "http://";

        for (String licenseUrl : useLicenseUrls) {
            try {
                final String useLicenseUrl = protocol + licenseUrl;
                final HttpConfigurable httpConfigurable = (HttpConfigurable) ApplicationManager.getApplication().getComponent("HttpConfigurable");
                final URLConnection urlConnection = httpConfigurable != null ? httpConfigurable.openConnection(useLicenseUrl) : new URL(useLicenseUrl).openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                urlConnection.connect();
                final OutputStream outputStream = urlConnection.getOutputStream();
                if (LOG_AGENT_INFO) System.out.println(licenseRequest.toJsonString());
                outputStream.write(licenseRequest.toJsonString().getBytes("UTF-8"));
                outputStream.flush();
                remove_license = false;

                try {
                    inputStream = urlConnection.getInputStream();
                    lastCommunicationsError = null;
                    break;
                } catch (IOException e) {
                    lastCommunicationsError = e.getMessage();
                } finally {
                    outputStream.close();
                }
            } catch (Throwable e) {
                lastCommunicationsError = e.getMessage();
            }
        }

        if (inputStream == null) return false;

        lastCommunicationsError = null;

        //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        //StringBuilder sb = new StringBuilder();
        //
        //String line = null;
        //try {
        //    while ((line = reader.readLine()) != null) {
        //        sb.append(line).append('\n');
        //    }
        //} catch (IOException e) {
        //    e.printStackTrace();
        //} finally {
        //    try {
        //        inputStream.close();
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }
        //}
        JsonReader jsonReader = Json.createReader(inputStream);
        json = jsonReader.readObject();
        String status = json.getString(STATUS, "");
        String message = json.getString(MESSAGE, "");
        if (status.equals(STATUS_OK)) {
            if ((licenseRequest.hasLicenseCode() || json.containsKey(LICENSE_CODE)) && json.containsKey(ACTIVATION_CODE)) {
                if (json.containsKey(LICENSE_CODE)) this.license_code = json.getString(LICENSE_CODE);
                this.activation_code = json.getString(ACTIVATION_CODE);
                return true;
            } else {
                if (LOG_AGENT_INFO) System.out.println("License server did not reply with a valid response");
            }
        } else {
            if (status.equals(STATUS_DISABLE)) {
                // remove license information from this plugin
                if (LOG_AGENT_INFO) System.out.println("License server requested license removal from this host");
                license_code = null;
                activation_code = null;
                activation = null;
                remove_license = true;
                status = STATUS_ERROR;
            }
            if (LOG_AGENT_INFO) System.out.println("License server replied with status: " + status + ", message: " + message);
        }

        return true;
    }

    public boolean isValidLicense() {
        /*if (license_code != null) {
            int headerPos = license_code.indexOf(licenseHeader);
            int footerPos = license_code.lastIndexOf(licenseFooter);
            if (headerPos >= 0 && footerPos > headerPos) {
                return true;
            }

            headerPos = license_code.indexOf(altLicenseHeader);
            footerPos = license_code.lastIndexOf(altLicenseFooter);
            if (headerPos >= 0 && footerPos > headerPos) {
                return true;
            }
        }*/
        return true;
    }

    public boolean isValidActivation() {
        if (activation_code != null) {
            if (activation != null) {
                if (isActivationExpired()) {
                    activation = null;
                    activation_code = null;
                    return false;
                }
            }

            if (activation == null) {
                String useActivationHeader = activationHeader;
                int headerPos = activation_code.indexOf(useActivationHeader);
                int footerPos = activation_code.lastIndexOf(activationFooter);

                if (!(headerPos >= 0 && footerPos > headerPos)) {
                    useActivationHeader = altActivationHeader;
                    headerPos = activation_code.indexOf(useActivationHeader);
                    footerPos = activation_code.lastIndexOf(altActivationFooter);
                }

                if (headerPos >= 0 && footerPos > headerPos) {
                    try {
                        LicenseKey licenseKey = new LicenseKey(license_pub);
                        String activationJson = licenseKey.decrypt(activation_code.substring(headerPos + useActivationHeader.length(), footerPos));

                        if (activationJson != null) {
                            byte[] bytes = activationJson.getBytes("UTF-8");
                            InputStream stream = new ByteArrayInputStream(bytes);
                            JsonReader jsonReader = Json.createReader(stream);
                            activation = jsonReader.readObject();
                        }
                    } catch (NoSuchPaddingException ignored) {
                    } catch (NoSuchAlgorithmException ignored) {
                    } catch (IOException ignored) {
                    } catch (InvalidKeySpecException ignored) {
                    } catch (Exception ignored) {
                    }
                }
            }

            if (activation != null && activation.containsKey(AGENT_SIGNATURE) && activation.getString(AGENT_SIGNATURE, "").equals(agent_signature)
                    && activation.containsKey(LICENSE_EXPIRES)
                    && activation.containsKey(LICENSE_TYPE)
                    && activation.containsKey(LICENSE_FEATURES)
                    && activation.containsKey(LICENSE_FEATURE_LIST)
                    && activation.containsKey(PRODUCT_VERSION)
                    && activation.containsKey(HOST_NAME)
                    && activation.containsKey(HOST_PRODUCT)
                    && activation.getString(LICENSE_TYPE) != null
                    && activation.getInt(LICENSE_FEATURES) != 0
                    ) {
                try {
                    license_expires = activation.getString(LICENSE_EXPIRES);
                    product_version = activation.getString(PRODUCT_VERSION);

                    if (activation.containsKey(PRODUCT_RELEASED_AT)) {
                        product_released_at = activation.getString(PRODUCT_RELEASED_AT);
                    } else {
                        product_released_at = "";
                    }

                    license_type = activation.getString(LICENSE_TYPE);
                    license_features = activation.getInt(LICENSE_FEATURES);
                    JsonObject feature_list = activation.getJsonObject(LICENSE_FEATURE_LIST);
                    featureList = new HashMap<String, Integer>();
                    for (Map.Entry<String, JsonValue> feature : feature_list.entrySet()) {
                        if (feature.getValue() instanceof JsonNumber) {
                            featureList.put(feature.getKey(), ((JsonNumber) feature.getValue()).intValue());
                        }
                    }
                    return true;
                } catch (JsonException ignored) {
                    if (LOG_AGENT_INFO) System.out.println("Activation JsonException " + ignored);
                } catch (ClassCastException ignored) {
                    if (LOG_AGENT_INFO) System.out.println("Activation ClassCastException " + ignored);
                } catch (Exception ignored) {
                    if (LOG_AGENT_INFO) System.out.println("Activation Exception " + ignored);
                }
            }
        }
        return false;
    }

    @NotNull
    public String getLicenseType() {
        return "License";
    }

    public int getLicenseFeatures() {
        return license_features;
    }

    @NotNull
    public String getLicenseExpiration() {
        if (activation != null && activation.containsKey(LICENSE_EXPIRES)) {
            return activation.getString(LICENSE_EXPIRES);
        }
        return "";
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
        if (activation != null && activation.containsKey(ACTIVATED_ON)) {
            return activation.getString(ACTIVATED_ON);
        }
        return "";
    }

    public int getLicenseExpiringIn() {
        // see if the license expiration is more than i days away
        /*if (activation != null && activation.containsKey(LICENSE_EXPIRES)) {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            try {
                String expires = activation.getString(LICENSE_EXPIRES);
                Date expiration = df.parse(expires);
                Date today = new Date();
                int days = (int) Math.floor((expiration.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
                return days + 1;
            } catch (ParseException ignored) {
            }
        }*/
        return 36000;//days
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
        // see if the activation has expired
//        if (activation != null) {
//            if (product_version == null || !product_version.equals(MultiMarkdownPlugin.getProductVersion())
//                    //|| !getHostName().equals(LicenseRequest.getHostName())
//                    || !getHostProduct().equals(LicenseRequest.getHostProduct())
//                    ) {
//                return true;
//            }
//
//            if (activation.containsKey(ACTIVATION_EXPIRES)) {
//                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
//                try {
//                    String expires = activation.getString(ACTIVATION_EXPIRES);
//                    Date expiration = df.parse(expires);
//                    Date today = new Date();
//                    int days = (int) floorDiv(expiration.getTime() - today.getTime(), (1000 * 60 * 60 * 24));
//                    return days < 0;
//                } catch (ParseException ignored) {
//                    return true;
//                }
//            }
//            return false;
//        }
        return false;
    }
}
