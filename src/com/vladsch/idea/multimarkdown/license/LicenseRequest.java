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

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.diagnostic.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by CrazyBunQnQ on 2017/6/10.
 */
public class LicenseRequest {
    //private static String host_system_name;
    private static final Logger logger = Logger.getInstance("com.vladsch.idea.multimarkdown.license.request");

    final public String product_name;
    final public String product_version;
    public String email;
    public String name;
    public String password;
    public String agent_signature;
    public String license_code;
    public String host_name;
    public String host_os;
    public String host_product;
    public String host_jre;

    public String toJsonString() {
        JsonStringer json = new JsonStringer();
        if (product_name != null) json.add("product_name", product_name);
        if (product_version != null) json.add("product_version", product_version);
        if (email != null) json.add("email", email);
        if (name != null) json.add("name", name);
        if (password != null) json.add("password", password);
        if (license_code != null) json.add("license_code", license_code);
        if (agent_signature != null) json.add("agent_signature", agent_signature);
        if (host_name != null) json.add("host_name", host_name);
        if (host_os != null) json.add("host_os", host_os);
        if (host_product != null) json.add("host_product", host_product);
        if (host_jre != null) json.add("host_jre", host_jre);
        return json.toString();
    }

    //interface RunnableTest {
    //    String result();
    //}
    //
    //public static void TestMethods(String name, RunnableTest canFail) {
    //    String result = "";
    //
    //    try {
    //        result = canFail.result();
    //    } catch (NoSuchMethodError ignored) {
    //        result = "NoSuchMethodError";
    //    }
    //    logger.info("getBuild()."+ name + " = " + ;
    //}
    //
    //    try {
    //        return ApplicationInfo.getInstance().getBuild().asStringWithAllDetails();
    //    } catch (NoSuchMethodError ignored) {
    //
    //    }
    //}

    public static String getHostProduct() {
        try {
            return ApplicationInfo.getInstance().getBuild().asString();
        } catch (NoSuchMethodError ignored) {

        }
        return "<unknown>";
    }

    public LicenseRequest(String product_name, String product_version) {
        this.product_name = product_name;
        this.product_version = product_version;
        this.host_name = "Hello World";
        this.host_product = "Hello World";
        this.host_os = "Hello World";
        this.host_jre = "Hello World";
    }

    public static String getHostName() {
        //if (host_system_name != null) {
        //    return host_system_name;
        //}

        // try InetAddress.LocalHost first;
        //      NOTE -- InetAddress.getLocalHost().getHostName() will not work in certain environments.
        String host = null;

        try {
            String result = InetAddress.getLocalHost().getHostName();
            if (result != null && !result.isEmpty()) {
                host = result;
            }
        } catch (UnknownHostException e) {
            // failed;  try alternate means.
        }

        // try environment properties.
        if (host == null) {
            host = System.getenv("COMPUTERNAME");

            if (host == null) {
                host = System.getenv("HOSTNAME");
                if (host == null) {
                    host = "undetermined";
                }
            }
        }

        // undetermined.
        //host_system_name = host;
        //return host + "_test";
        return host;
    }

    public boolean hasLicenseCode() {
        return license_code != null && !license_code.isEmpty();
    }
}
