package org.isf.generaldata;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

public class PropertyReader {

	private Logger logger;

	private static Map<Class, PropertyExtractor> typePropertyExtractorMap = new HashMap<Class, PropertyExtractor>();

	public PropertyReader(Map properties, Logger logger) {
		this.logger = logger;

		typePropertyExtractorMap.put(String.class, new StringPropertyExtractor(
				properties));
		typePropertyExtractorMap.put(int.class, new IntPropertyExtractor(
				properties));
		typePropertyExtractorMap.put(boolean.class, new BooleanPropertyExtractor(
				properties));
	}

	/**
	 * 
	 * Method to retrieve a property
	 * 
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	public <T> T readProperty(String propertyName, T defaultValue) {
		T value;

		try {
			value = (T) typePropertyExtractorMap.get(defaultValue.getClass())
					.getProperty(propertyName);
		} catch (Exception e) {
			this.logger.warn(">> " + propertyName + " property not found: default is " + defaultValue);
			return defaultValue;
		}
		
		return value;
	}
	
	interface PropertyExtractor<T> {
		T getProperty(String propertyName);
	}

	class StringPropertyExtractor implements PropertyExtractor<String> {
		private Map properties;

		public StringPropertyExtractor(Map properties) {
			this.properties = properties;
		}

		@Override
		public String getProperty(String propertyName) {
			return this.properties.get(propertyName).toString();
		}
	}

	class IntPropertyExtractor implements PropertyExtractor<Integer> {
		private Map properties;

		public IntPropertyExtractor(Map properties) {
			this.properties = properties;
		}

		@Override
		public Integer getProperty(String propertyName) {
			return (Integer)(this.properties.get(propertyName));
		}
	}

	class BooleanPropertyExtractor implements PropertyExtractor<Boolean> {
		private Map properties;

		public BooleanPropertyExtractor(Map properties) {
			this.properties = properties;
		}

		@Override
		public Boolean getProperty(String propertyName) {
			return this.properties.get(propertyName).toString().equalsIgnoreCase("YES");
		}
	}
}


