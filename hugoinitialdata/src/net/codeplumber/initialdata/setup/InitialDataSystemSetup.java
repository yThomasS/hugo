/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package net.codeplumber.initialdata.setup;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import static net.codeplumber.initialdata.constants.HugoInitialDataConstants.EXTENSIONNAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This class provides hooks into the system's initialization and update processes.
 */
@SystemSetup(extension = EXTENSIONNAME)
public class InitialDataSystemSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(InitialDataSystemSetup.class);

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	private static final String IMPORT_HUGO_SHOES = "importHugoShoes";
	private static final String SAMPLE_CONTENT_CATALOG_NAME = "hugoContentCatalog";
	private static final String SAMPLE_PRODUCT_CATALOG_NAME = "hugoProductCatalog";
	private static final String SAMPLE_STORE_NAME = "hugoStore";

	private HugoCoreDataImportService hugoCoreDataImportService;
	private HugoSampleDataImportService hugoSampleDataImportService;

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_HUGO_SHOES, "Import Hugos Shoe Products", true));
		// Add more Parameters here as you require

		return params;
	}

	/**
	 * Implement this method to create initial objects. This method will be called by system creator during
	 * initialization and system update. Be sure that this method can be called repeatedly.
	 * 
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		// Add Essential Data here as you require
	}

	/**
	 * Implement this method to create data that is used in your project. This method will be called during the system
	 * initialization. <br>
	 * Add import data for each site you have configured
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		/*
		 * Add import data for each site you have configured
		 */

		LOG.info("Create Hugo Shoes Project Data");
		if (context.getParameterMap().containsKey(EXTENSIONNAME + "_" + IMPORT_HUGO_SHOES)) {
			if ("true".equalsIgnoreCase(context.getParameterMap().get(EXTENSIONNAME + "_" + IMPORT_HUGO_SHOES)[0])) {
				LOG.info("Loading impex file");
				getSetupImpexService().importImpexFile(
						String.format("/%s/import/sampledata/hugo-products-shoes.impex", EXTENSIONNAME), true);
			}
		} else {
			LOG.info("Parameter is set to false or key does not exists");
		}

		final List<ImportData> importData = new ArrayList<ImportData>();

	  final ImportData sampleImportData = new ImportData();
	  sampleImportData.setProductCatalogName(SAMPLE_PRODUCT_CATALOG_NAME);
	  sampleImportData.setContentCatalogNames(Arrays.asList(SAMPLE_CONTENT_CATALOG_NAME));
	  sampleImportData.setStoreNames(Arrays.asList(SAMPLE_STORE_NAME));
	  importData.add(sampleImportData);

	  getHugoCoreDataImportService().execute(this, context, importData);
	  getEventService().publishEvent(new CoreDataImportedEvent(context, importData));

	  getHugoSampleDataImportService().execute(this, context, importData);
	  getEventService().publishEvent(new SampleDataImportedEvent(context, importData));

	}

	public HugoCoreDataImportService getHugoCoreDataImportService()
	{
		return hugoCoreDataImportService;
	}

	@Required
	public void setHugoCoreDataImportService(final HugoCoreDataImportService hugoCoreDataImportService)
	{
		this.hugoCoreDataImportService = hugoCoreDataImportService;
	}

	public HugoSampleDataImportService getHugoSampleDataImportService()
	{
		return hugoSampleDataImportService;
	}

	@Required
	public void setHugoSampleDataImportService(final HugoSampleDataImportService hugoSampleDataImportService)
	{
		this.hugoSampleDataImportService = hugoSampleDataImportService;
	}
}
