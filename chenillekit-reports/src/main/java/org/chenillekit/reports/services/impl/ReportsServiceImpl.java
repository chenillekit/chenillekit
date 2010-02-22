/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.reports.services.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;

import org.apache.tapestry5.ioc.Resource;
import org.chenillekit.reports.services.ReportsService;
import org.chenillekit.reports.utils.ExportFormat;
import org.slf4j.Logger;

/**
 * reporting tool that has the ability to deliver rich content to the screen or printer or into PDF, HTML, XLS, CSV and XML files
 * based <a href="http://jasperforge.org/sf/projects/jasperreports">jasperreports</a>.
 *
 * @version $Id$
 */
public class ReportsServiceImpl implements ReportsService
{
	private Logger logger;

	public ReportsServiceImpl(Logger logger, List<URL> configurations)
	{
		if (configurations.isEmpty())
			throw new RuntimeException("Configurations for JasperReports needed");
		
		this.logger = logger;

		try
		{
			JRProperties.setProperty(JRProperties.COMPILER_TEMP_DIR, System.getProperty("java.io.tmpdir"));
			
			Properties properties = new Properties();
			
			for (URL url : configurations)
			{
				properties.load(url.openStream());
			}
			
			Enumeration enumeration = properties.keys();
			while (enumeration.hasMoreElements())
			{
				String key = (String) enumeration.nextElement();
				JRProperties.setProperty(key.toUpperCase(), properties.getProperty(key));
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * set the classpath for the choosen jasperReport compiler.
	 * all JARs located in the given directory will added to the actual classpath from the running application.
	 *
	 * @param value name of the location, where your additional libraries resists.
	 */
	public void addCompilerClassPath(String value)
	{
		File jarDir = new File(value);
		if (jarDir.exists() && jarDir.isDirectory())
		{
			File[] jarFiles = jarDir.listFiles(new FilenameFilter()
			{

				/**
				 * Tests if a specified file should be included in a file list.
				 *
				 * @param dir  the directory in which the file was found.
				 * @param name the name of the file.
				 *
				 * @return <code>true</code> if and only if the name should be
				 *         included in the file list; <code>false</code> otherwise.
				 */
				public boolean accept(File dir, String name)
				{
					return name.endsWith(".jar");
				}
			});

			for (File jarFile : jarFiles)
				addCompilerClassPath(jarFile.getPath());
		}
		else
		{
			if (logger.isWarnEnabled())
				logger.warn("'{}' does not exists or is not a directory", jarDir.getPath());
		}
	}

	/**
	 * get the choosen reporting output engine.
	 *
	 * @param format
	 *
	 * @return JRAbstractExporter reporting output engine
	 */
	protected JRAbstractExporter getJasperExporter(ExportFormat format)
	{
		JRAbstractExporter abstractExporter = null;

		switch (format)
		{
			case PDF:
				abstractExporter = new JRPdfExporter();
				break;

			case RTF:
				abstractExporter = new JRRtfExporter();
				break;

			case ODT:
				abstractExporter = new JROdtExporter();
				break;

			case HTML:
				abstractExporter = new JRHtmlExporter();
				break;

			case CSV:
				abstractExporter = new JRCsvExporter();
				break;

			case XML:
				abstractExporter = new JRXmlExporter();
				break;
		}

		return abstractExporter;
	}

	/**
	 * export the report.
	 *
	 * @param inputResource the input resource (report template file ".jrxml")
	 * @param format		the output format
	 * @param parameterMap  the parameter map
	 * @param dataSource	the datasource, maybe null or empty.
	 * @param outputStream  the output stream
	 */
	public void fillAndExport(Resource inputResource, ExportFormat format, Map parameterMap, JRDataSource dataSource, OutputStream outputStream)
	{
		JasperPrint jasperPrint = fillReport(inputResource, parameterMap, dataSource);
		export(jasperPrint, format, outputStream);
	}

	/**
	 * export the report.
	 *
	 * @param jasperPrint  represents a page-oriented document that can be viewed, printed or exported to other formats.
	 * @param format	   the output format
	 * @param outputStream the output stream
	 */
	public void export(JasperPrint jasperPrint, ExportFormat format, OutputStream outputStream)
	{
		JRAbstractExporter exporter = getJasperExporter(format);
		if (exporter == null)
			throw new RuntimeException("jasper service dont know about the output-type.");

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);

		if (exporter instanceof JRHtmlExporter)
		{
			exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
			exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, "20");
		}

		try
		{
			exporter.exportReport();
		}
		catch (JRException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Fills the report design loaded from the supplied input resource and returns the generated report object.
	 *
	 * @param inputResource the input resource (report template file ".jrxml")
	 * @param parameterMap  the parameter map
	 *
	 * @return
	 */
	public JasperPrint fillReport(Resource inputResource, Map parameterMap)
	{
		return fillReport(null, inputResource, parameterMap, null);
	}

	/**
	 * Fills the report design loaded from the supplied input resource and returns the generated report object.
	 *
	 * @param inputResource the input resource (report template file ".jrxml")
	 * @param parameterMap  the parameter map
	 * @param dataSource	the datasource, maybe null.
	 *
	 * @return
	 */
	public JasperPrint fillReport(Resource inputResource, Map parameterMap, JRDataSource dataSource)
	{
		return fillReport(null, inputResource, parameterMap, dataSource);
	}

	/**
	 * Fills the report design loaded from the supplied input resource and returns the generated report object.
	 * <p/>
	 * if parameter <em>jasperPrint<em> not null, the data filled into <em>jasperPrint</em>
	 * instead of returns a new jasper print object.
	 *
	 * @param jasperPrint
	 * @param inputResource the input resource (report template file ".jrxml")
	 * @param parameterMap  the parameter map
	 * @param dataSource	the datasource, maybe null.
	 *
	 * @return
	 */
	public JasperPrint fillReport(JasperPrint jasperPrint, Resource inputResource, Map parameterMap, JRDataSource dataSource)
	{
		JasperReport jasperReport = doCompileReportSource(inputResource);
		JasperPrint actualJasperPrint;

		try
		{
			if (dataSource != null)
				actualJasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, dataSource);
			else
				actualJasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap);

			if (jasperPrint != null)
			{
				for (Object page : actualJasperPrint.getPages())
					jasperPrint.addPage((JRPrintPage) page);
			}
			else
				jasperPrint = actualJasperPrint;

			return jasperPrint;
		}
		catch (JRException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * compiles jasper template file.
	 * compiles template file only if input file is new than output file.
	 *
	 * @param template template resource
	 *
	 * @return file name of  compiled report
	 */
	protected JasperReport doCompileReportSource(Resource template)
	{
		boolean sourceMustCompile = false;

		try
		{
			if (logger.isDebugEnabled())
				logger.debug("using template '{}' for report.", template.toURL());

			File sourceFile = new File(template.toURL().toURI());
			File compiledFile = new File(JRProperties.getProperty(JRProperties.COMPILER_TEMP_DIR) + "/" +
					sourceFile.getName() + ".jasper");

			if (!sourceFile.exists())
				throw new RuntimeException("ReportSource '" + sourceFile.getAbsolutePath() + "' is missing!");

			else
			{
				if (compiledFile.exists())
				{
					if (sourceFile.lastModified() > compiledFile.lastModified())
						sourceMustCompile = true;
				}
				else
					sourceMustCompile = true;
			}

			if (sourceMustCompile)
			{
				if (logger.isDebugEnabled())
					logger.debug("compiling jasper template {} ...", sourceFile.toURL());

				long startCompile = System.currentTimeMillis();

				JasperCompileManager.compileReportToFile(sourceFile.getPath(), compiledFile.getPath());

				long endCompile = System.currentTimeMillis();

				if (logger.isInfoEnabled())
					logger.info("template '{}' needs {}ms to compile", template.toURL(), endCompile - startCompile);
			}

			return (JasperReport) JRLoader.loadObject(compiledFile);
		}
		catch (JRException e)
		{
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
		catch (URISyntaxException e)
		{
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
		catch (MalformedURLException e)
		{
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
	}
}
