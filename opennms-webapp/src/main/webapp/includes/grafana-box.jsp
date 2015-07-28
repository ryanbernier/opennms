<%--
/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2002-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

--%>

<%--
  This page is included by other JSPs to create a box containing a list of grafana dashboards.

  It expects that a <base> tag has been set in the including page
  that directs all URLs to be relative to the servlet context.
--%>

<%@page language="java"
        contentType="text/html"
        session="true"
        import="org.apache.commons.io.IOUtils,
                org.apache.http.HttpHost,
                org.apache.http.HttpResponse,
                org.apache.http.client.methods.HttpGet,
                org.apache.http.conn.ConnectTimeoutException,
                org.apache.http.impl.client.DefaultHttpClient,
                org.apache.http.params.HttpConnectionParams,
                org.apache.http.params.HttpParams,
                java.net.UnknownHostException,
                java.net.ConnectException" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    String grafanaApiKey = System.getProperty("org.opennms.grafanaBox.apiKey", "");
    String grafanaProtocol = System.getProperty("org.opennms.grafanaBox.protocol", "http");
    String grafanaHostname = System.getProperty("org.opennms.grafanaBox.hostname", "localhost");
    String grafanaTag = System.getProperty("org.opennms.grafanaBox.tag", "");
    int grafanaPort = Integer.parseInt(System.getProperty("org.opennms.grafanaBox.port", "3000"));
    int grafanaConnectionTimeout = Integer.parseInt(System.getProperty("org.opennms.grafanaBox.connectionTimeout", "500"));
    int grafanaSoTimeout = Integer.parseInt(System.getProperty("org.opennms.grafanaBox.soTimeout", "500"));
    String errorMessage = null;
    String responseString = null;

    if (!"".equals(grafanaApiKey)
            && !"".equals(grafanaHostname)
            && !"".equals(grafanaProtocol)
            && ("http".equals(grafanaProtocol) || "https".equals(grafanaProtocol))) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParams = httpClient.getParams();
            /**
             * Setting the timeouts to assure that the landing page will be loaded. Making the
             * call via JS isn't possible due to CORS-related problems with the Grafana server.
             */
            HttpConnectionParams.setConnectionTimeout(httpParams, grafanaConnectionTimeout);
            HttpConnectionParams.setSoTimeout(httpParams, grafanaSoTimeout);
            HttpHost httpHost = new HttpHost(grafanaHostname, grafanaPort, grafanaProtocol);
            HttpGet httpGet = new HttpGet("/api/search/");
            httpGet.setHeader("Authorization", "Bearer " + grafanaApiKey);
            HttpResponse httpResponse = httpClient.execute(httpHost, httpGet);
            responseString = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    } else {
        errorMessage = "Invalid configuration";
    }
%>

<div id="grafana-box" class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">Grafana Dashboards</h3>
    </div>

    <div id="dashboardlist" class="panel-body">
        <%
            if (responseString != null) {
        %>
        <script type="text/javascript">
            var grafanaTag = '<%=grafanaTag%>';
            var obj = <%=responseString%>;

            jQuery.each(obj['dashboards'], function (i, val) {
                var showDashboard = true;

                if (grafanaTag != '') {
                    showDashboard = false;

                    for (var index in val['tags']) {
                        if (grafanaTag == val['tags'][index]) {
                            showDashboard = true;
                            break;
                        }
                    }
                }
                if (showDashboard) {
                    $('#dashboardlist').append('<a href="<%=grafanaProtocol%>://<%=grafanaHostname%>:<%=grafanaPort%>/dashboard/db/' + val['slug'] + '"><span class="glyphicon glyphicon-signal" aria-hidden="true"></span>&nbsp;' + val['title'] + "</a><br/>");
                }
            });
        </script>
        <%
            } else {
        %>
        <span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>&nbsp;<%=errorMessage%><br/>
        <%
            }
        %>
    </div>
</div>
