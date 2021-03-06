package com.sirmaenterprise.sep.eai.spreadsheet.service.rest;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sirma.itt.seip.permissions.InstanceAccessEvaluator;
import com.sirma.itt.seip.rest.secirity.SecurityTokensManager;
import com.sirma.itt.seip.rest.utils.JwtConfiguration;
import com.sirma.itt.seip.security.context.SecurityContext;
import com.sirmaenterprise.sep.eai.spreadsheet.configuration.SpreadsheetIntegrationConfiguration;

/**
 * {@link ContentToolProviderRest} is provider of jnlp file for the EAI content tool. Needed jar libraries are described
 * in the jnlp content and are served as web resources as part of the current module. a
 *
 * @author bbanchev
 */
@Singleton
@Path("/integration/content/tool")
public class ContentToolProviderRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentToolProviderRest.class);

	@Inject
	private SecurityContext securityContext;

	@Inject
	private SecurityTokensManager tokensManager;

	@Inject
	private JwtConfiguration jwtConfiguration;

	@Inject
	private InstanceAccessEvaluator instanceAccessEvaluator;

	@Inject
	private SpreadsheetIntegrationConfiguration spreadsheetIntegrationConfiguration;

	/**
	 * Builds the JNLP descriptor for the content tool.
	 *
	 * @param uriDetails is the current uri request
	 * @param instanceId is the processed spreadsheet
	 * @return the jnlp file as string and as "application/x-java-jnlp-file" content
	 */
	@GET
	@Transactional
	@Path("/descriptor")
	@Produces("application/x-java-jnlp-file")
	public Response loadTool(@Context UriInfo uriDetails, @QueryParam("id") String instanceId) {
		// check if user has write access to the requited spreadsheet - if not tool could be not be used
		if (!instanceAccessEvaluator.canWrite(instanceId)) {
			return Response.status(401).entity("Could not access for modification instance with id: " + instanceId)
					.build();
		}

		String jwtToken = tokensManager.generate(securityContext.getAuthenticated());

		// fill the template with the required arguments.
		String jnlp = null;
		try {
			String fileToString = FileUtils.readFileToString(spreadsheetIntegrationConfiguration.getContentToolJNLP().get(), StandardCharsets.UTF_8);
			jnlp = String.format(fileToString,
					uriDetails.resolve(URI.create("../eai/")),
					"eai-content-tool-jfx.jar?" + jwtConfiguration.getJwtParameterName() + "=" + jwtToken,
					uriDetails.getBaseUri().toString(), "Jwt " + jwtToken, instanceId);
		} catch (IOException e) {
			LOGGER.error("unable to read content import tool descriptor", e);
			return Response.serverError().entity("unable to read content import tool descriptor").build();
		}

		// set the mimetype
		return Response.ok(jnlp).header("Content-Type", "application/x-java-jnlp-file").build();
	}
}
