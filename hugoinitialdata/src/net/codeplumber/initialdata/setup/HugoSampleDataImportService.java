package net.codeplumber.initialdata.setup;

import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.InputStream;
import java.util.List;

/**
 * @author thomas.schroeder@hybris.com on 2018-03-05.
 */
public class HugoSampleDataImportService extends HugoDataImportService {

    public static final String IMPORT_SAMPLE_DATA = "importSampleData";
    public static final String BTG_EXTENSION_NAME = "btg";
    public static final String CMS_COCKPIT_EXTENSION_NAME = "cmscockpit";
    public static final String PRODUCT_COCKPIT_EXTENSION_NAME = "productcockpit";
    public static final String CS_COCKPIT_EXTENSION_NAME = "cscockpit";
    public static final String REPORT_COCKPIT_EXTENSION_NAME = "reportcockpit";
    public static final String MCC_EXTENSION_NAME = "mcc";
    public static final String CUSTOMER_SUPPORT_BACKOFFICE_EXTENSION_NAME = "customersupportbackoffice";
    public static final String ORDER_MANAGEMENT_BACKOFFICE_EXTENSION_NAME = "ordermanagementbackoffice";
    public static final String ASSISTED_SERVICE_EXTENSION_NAME = "assistedservicestorefront";

    @Override
    public void execute(final AbstractSystemSetup systemSetup, final SystemSetupContext context, final List<ImportData> importData)
    {
        final boolean importSampleData = systemSetup.getBooleanSystemSetupParameter(context, IMPORT_SAMPLE_DATA);

        if (importSampleData)
        {
            for (final ImportData data : importData)
            {
                importAllData(systemSetup, context, data, true);
            }
        }
    }

    @Override
    protected void importCommonData(final String extensionName)
    {
        if (isExtensionLoaded(CMS_COCKPIT_EXTENSION_NAME))
        {
            getSetupImpexService().importImpexFile(
                    String.format("/%s/import/sampledata/cockpits/cmscockpit/cmscockpit-users.impex", extensionName), false);
        }

        if (isExtensionLoaded(PRODUCT_COCKPIT_EXTENSION_NAME))
        {
            getSetupImpexService().importImpexFile(
                    String.format("/%s/import/sampledata/cockpits/productcockpit/productcockpit-users.impex", extensionName), false);
        }

        if (isExtensionLoaded(CS_COCKPIT_EXTENSION_NAME))
        {
            getSetupImpexService().importImpexFile(
                    String.format("/%s/import/sampledata/cockpits/cscockpit/cscockpit-users.impex", extensionName), false);
        }

        if (isExtensionLoaded(REPORT_COCKPIT_EXTENSION_NAME))
        {
            getSetupImpexService().importImpexFile(
                    String.format("/%s/import/sampledata/cockpits/reportcockpit/reportcockpit-users.impex", extensionName), false);

            if (isExtensionLoaded(MCC_EXTENSION_NAME))
            {
                getSetupImpexService().importImpexFile(
                        String.format("/%s/import/sampledata/cockpits/reportcockpit/reportcockpit-mcc-links.impex", extensionName),
                        false);
            }
        }

        if (isExtensionLoaded(CUSTOMER_SUPPORT_BACKOFFICE_EXTENSION_NAME)
                || isExtensionLoaded(ORDER_MANAGEMENT_BACKOFFICE_EXTENSION_NAME))
        {
            getSetupImpexService().importImpexFile(
                    String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-groups.impex", extensionName),
                    false);

            getSetupImpexService().importImpexFile(
                    String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-users.impex", extensionName),
                    false);
            getSetupImpexService()
                    .importImpexFile(
                            String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-savedqueries.impex",
                                    extensionName), false);
            getSetupImpexService()
                    .importImpexFile(
                            String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-accessrights.impex",
                                    extensionName), false);
            getSetupImpexService()
                    .importImpexFile(
                            String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-restrictions.impex",
                                    extensionName), false);
        }

        if (isExtensionLoaded(CUSTOMER_SUPPORT_BACKOFFICE_EXTENSION_NAME) && isExtensionLoaded(ASSISTED_SERVICE_EXTENSION_NAME))
        {
            getSetupImpexService().importImpexFile(
                    String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-assistedservice-groups.impex",
                            extensionName), false);
        }
    }

    @Override
    protected void importProductCatalog(final String extensionName, final String productCatalogName)
    {
        // Load Units
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/classifications-units.impex", extensionName,
                        productCatalogName), false);

        // Load Categories
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/categories.impex", extensionName,
                        productCatalogName), false);

        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/categories-classifications.impex",
                        extensionName, productCatalogName), false);

        // Load Suppliers
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/suppliers.impex", extensionName,
                        productCatalogName), false);
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/suppliers-media.impex", extensionName,
                        productCatalogName), false);

        // Load medias for Categories as Suppliers loads some new Categories
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/categories-media.impex", extensionName,
                        productCatalogName), false);

        // Load Products
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/products.impex", extensionName,
                        productCatalogName), false);
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/products-media.impex", extensionName,
                        productCatalogName), false);
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/products-classifications.impex", extensionName,
                        productCatalogName), false);

        // Load Products Relations
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/products-relations.impex", extensionName,
                        productCatalogName), false);

        // Load Products Fixes
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/products-fixup.impex", extensionName,
                        productCatalogName), false);

        // Load Prices
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/products-prices.impex", extensionName,
                        productCatalogName), false);

        // Load Stock Levels
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/products-stocklevels.impex", extensionName,
                        productCatalogName), false);
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/products-pos-stocklevels.impex", extensionName,
                        productCatalogName), false);

        // Load Taxes
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/products-tax.impex", extensionName,
                        productCatalogName), false);

        // Load Multi-Dimensial Products
        importMultiDProductCatalog(extensionName, productCatalogName);

    }

    protected void importMultiDProductCatalog(final String extensionName, final String productCatalogName)
    {
        // Load Multi-Dimension Categories
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/multi-d/dimension-categories.impex",
                        extensionName, productCatalogName), false);
        // Load Multi-Dimension Products
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/multi-d/dimension-products.impex",
                        extensionName, productCatalogName), false);
        // Load Multi-Dimension Products-Media
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/multi-d/dimension-products-media.impex",
                        extensionName, productCatalogName), false);
        // Load Multi-Dimension Products-Prices
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/multi-d/dimension-products-prices.impex",
                        extensionName, productCatalogName), false);
        // Load Multi-Dimension Products-Stocklevels
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/multi-d/dimension-products-stock-levels.impex",
                        extensionName, productCatalogName), false);
        // Load Multi-Dimension Products-Stocklevels
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/multi-d/dimension-products-tax.impex",
                        extensionName, productCatalogName), false);
        // Load Multi-Dimension Products-pos-stocklevels
        getSetupImpexService().importImpexFile(
                String.format(
                        "/%s/import/sampledata/productCatalogs/%s/multi-d/dimension-products-pos-stocklevels.impex",
                        extensionName, productCatalogName), false);
        // Load Multi-Dimension Products-classifications
        getSetupImpexService().importImpexFile(
                String.format(
                        "/%s/import/sampledata/productCatalogs/%s/multi-d/dimension-products-classifications.impex",
                        extensionName, productCatalogName), false);
        // Load future stock for multi -D products
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/products-futurestock.impex", extensionName,
                        productCatalogName), false);
    }

    @Override
    protected void importContentCatalog(final String extensionName, final String contentCatalogName)
    {
        if (isResponsive())
        {
            final String responsiveContentFile = String.format(
                    "/%s/import/sampledata/contentCatalogs/%s/cms-responsive-content.impex", extensionName,
                    contentCatalogName);
            if (getInputStream(responsiveContentFile) != null)
            {
                getSetupImpexService().importImpexFile(responsiveContentFile, false);
            }
            else
            {
                getSetupImpexService().importImpexFile(
                        String.format("/%s/import/sampledata/contentCatalogs/%s/cms-content.impex", extensionName,
                                contentCatalogName), false);
            }
        }
        else
        {
            getSetupImpexService().importImpexFile(
                    String.format("/%s/import/sampledata/contentCatalogs/%s/cms-content.impex", extensionName,
                            contentCatalogName), false);

            if (getConfigurationService().getConfiguration().getBoolean(IMPORT_MOBILE_DATA, false))
            {
                getSetupImpexService().importImpexFile(
                        String.format("/%s/import/sampledata/contentCatalogs/%s/cms-mobile-content.impex", extensionName,
                                contentCatalogName), false);
            }
        }

        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/contentCatalogs/%s/email-content.impex", extensionName,
                        contentCatalogName), false);

    }

    @Override
    protected void importStore(final String extensionName, final String storeName, final String productCatalogName)
    {
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/stores/%s/points-of-service-media.impex", extensionName, storeName), false);
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/stores/%s/points-of-service.impex", extensionName, storeName), false);
        if (isExtensionLoaded(BTG_EXTENSION_NAME))
        {
            getSetupImpexService().importImpexFile(
                    String.format("/%s/import/sampledata/stores/%s/btg.impex", extensionName, storeName), false);
        }
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/stores/%s/warehouses.impex", extensionName, storeName), false);

        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/productCatalogs/%s/reviews.impex", extensionName,
                        productCatalogName), false);

        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/stores/%s/promotions.impex", extensionName, storeName), false);

        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/stores/%s/consents.impex", extensionName, storeName), false);
    }

    @Override
    protected void importJobs(final String extensionName, final String storeName)
    {
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/stores/%s/jobs.impex", extensionName, storeName), false);
    }

    @Override
    protected void importSolrIndex(final String extensionName, final String storeName)
    {
        getSetupImpexService().importImpexFile(
                String.format("/%s/import/sampledata/stores/%s/solr.impex", extensionName, storeName), false);

        getSetupSolrIndexerService().createSolrIndexerCronJobs(String.format("%sIndex", storeName));
    }

    protected InputStream getInputStream(final String fileName)
    {
        return getClass().getResourceAsStream(fileName);
    }

    protected boolean isResponsive()
    {
        return ResponsiveUtils.isResponsive();
    }

    @Override
    public boolean synchronizeContentCatalog(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
                                             final String catalogName, final boolean syncCatalogs)
    {
        systemSetup.logInfo(context, String.format("Begin (hugo) synchronizing Content Catalog [%s]", catalogName));

        getSetupSyncJobService().createContentCatalogSyncJob(String.format("%s", catalogName));

        if (syncCatalogs)
        {
            final PerformResult syncCronJobResult = getSetupSyncJobService().executeCatalogSyncJob(
                    String.format("%s", catalogName));
            if (isSyncRerunNeeded(syncCronJobResult))
            {
                systemSetup.logInfo(context, String.format("Content Catalog [%s] sync has issues.", catalogName));
                return false;
            }
        }

        return true;
    }
}
