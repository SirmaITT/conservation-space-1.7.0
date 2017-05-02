package com.sirma.itt.emf.link.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONObject;

import com.sirma.itt.commons.utils.string.StringUtils;
import com.sirma.itt.seip.convert.Converter;
import com.sirma.itt.seip.convert.TypeConverter;
import com.sirma.itt.seip.convert.TypeConverterProvider;
import com.sirma.itt.seip.domain.definition.DataTypeDefinition;
import com.sirma.itt.seip.domain.instance.EmfInstance;
import com.sirma.itt.seip.domain.instance.Instance;
import com.sirma.itt.seip.exception.EmfConfigurationException;
import com.sirma.itt.seip.instance.dao.InstanceDao;
import com.sirma.itt.seip.instance.dao.ServiceRegistry;
import com.sirma.itt.seip.instance.relation.LinkInstance;
import com.sirma.itt.seip.model.LinkSourceId;

/**
 * Converter provider class to enable conversion from {@link LinkSourceId} to
 * concrete. {@link Instance}.
 *
 * @author BBonev
 */
@ApplicationScoped
public class LinkSourceToInstanceConverterProvider implements TypeConverterProvider {

	/** The service register. */
	@Inject
	private ServiceRegistry serviceRegistry;

	/**
	 * Converter class to support conversion from {@link LinkSourceId} to
	 * concrete {@link Instance} implementation.
	 *
	 * @author BBonev
	 */
	public class LinkSourceToInstanceConverter implements Converter<LinkSourceId, Instance> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Instance convert(LinkSourceId source) {
			DataTypeDefinition type = source.getSourceType();
			if (type == null) {
				throw new EmfConfigurationException(
						"Cannot convert " + LinkSourceId.class.getCanonicalName() + " to unknown type!");
			}
			String id = source.getSourceId();
			if (StringUtils.isNullOrEmpty(id)) {
				throw new EmfConfigurationException("Cannot convert " + LinkSourceId.class.getCanonicalName()
						+ " to non persisted " + type.getJavaClassName() + " type!");
			}
			Class<?> clazz = type.getJavaClass();
			if (clazz != null) {
				Instance instance = convertInternal(id, clazz);
				if (instance instanceof EmfInstance) {
					// set the source reference
					((EmfInstance) instance).setReference(source);
				}
				return instance;
			}
			throw new EmfConfigurationException("Convertion of " + LinkSourceId.class.getCanonicalName() + " to "
					+ type.getJavaClassName() + " is not supported!");
		}

		/**
		 * Performs the internal conversion. If not supported conversion returns
		 * <code>null</code>
		 *
		 * @param id
		 *            the id of the instance to load
		 * @param sourceClass
		 *            the source class
		 * @return the loaded instance or <code>null</code> it not able to
		 *         handle it
		 */
		protected Instance convertInternal(String id, Class<?> sourceClass) {
			if (sourceClass.equals(LinkInstance.class)) {
				// we could load the entity and convert it to instance
				throw new EmfConfigurationException("Conversion of " + LinkSourceId.class.getCanonicalName() + " to "
						+ LinkInstance.class.getCanonicalName() + " is not supported, yet.");
			}
			InstanceDao instanceDao = serviceRegistry.getInstanceDao(sourceClass);
			if (instanceDao != null) {
				try {
					if (instanceDao.getPrimaryIdType().equals(String.class)) {
						return instanceDao.loadInstance(id, null, false);
					}
					if (!Character.isDigit(id.charAt(0))) {
						// some optimization when converting the identifiers
						return instanceDao.loadInstance(null, id, false);
					}
					long dbId = Long.parseLong(id);
					return instanceDao.loadInstance(dbId, null, false);
				} catch (NumberFormatException e) {
					return instanceDao.loadInstance(null, id, false);
				}
			}
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void register(TypeConverter converter) {
		converter.addConverter(LinkSourceId.class, Instance.class, new LinkSourceToInstanceConverter());

		converter.addConverter(LinkSourceId.class, String.class, linkSource -> linkSource.toJSONObject().toString());
		converter.addConverter(LinkSourceId.class, JSONObject.class, linkSource -> linkSource.toJSONObject());
	}

}