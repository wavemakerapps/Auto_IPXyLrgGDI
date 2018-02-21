/*Copyright (c) 2015-2016 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.auto_ipxylrggdi.hrdb.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.model.Downloadable;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import com.auto_ipxylrggdi.hrdb.Employee;
import com.auto_ipxylrggdi.hrdb.Vacation;
import com.auto_ipxylrggdi.hrdb.service.EmployeeService;


/**
 * Controller object for domain model class Employee.
 * @see Employee
 */
@RestController("hrdb.EmployeeController")
@Api(value = "EmployeeController", description = "Exposes APIs to work with Employee resource.")
@RequestMapping("/hrdb/Employee")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
	@Qualifier("hrdb.EmployeeService")
	private EmployeeService employeeService;

	@ApiOperation(value = "Creates a new Employee instance.")
    @RequestMapping(method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Employee createEmployee(@RequestBody Employee employee) {
		LOGGER.debug("Create Employee with information: {}" , employee);

		employee = employeeService.create(employee);
		LOGGER.debug("Created Employee with information: {}" , employee);

	    return employee;
	}

    @ApiOperation(value = "Returns the Employee instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Employee getEmployee(@PathVariable("id") Integer id) throws EntityNotFoundException {
        LOGGER.debug("Getting Employee with id: {}" , id);

        Employee foundEmployee = employeeService.getById(id);
        LOGGER.debug("Employee details with id: {}" , foundEmployee);

        return foundEmployee;
    }

    @ApiOperation(value = "Updates the Employee instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PUT)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Employee editEmployee(@PathVariable("id") Integer id, @RequestBody Employee employee) throws EntityNotFoundException {
        LOGGER.debug("Editing Employee with id: {}" , employee.getEmpId());

        employee.setEmpId(id);
        employee = employeeService.update(employee);
        LOGGER.debug("Employee details with id: {}" , employee);

        return employee;
    }

    @ApiOperation(value = "Deletes the Employee instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.DELETE)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public boolean deleteEmployee(@PathVariable("id") Integer id) throws EntityNotFoundException {
        LOGGER.debug("Deleting Employee with id: {}" , id);

        Employee deletedEmployee = employeeService.delete(id);

        return deletedEmployee != null;
    }

    /**
     * @deprecated Use {@link #findEmployees(String, Pageable)} instead.
     */
    @Deprecated
    @ApiOperation(value = "Returns the list of Employee instances matching the search criteria.")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Employee> searchEmployeesByQueryFilters( Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering Employees list");
        return employeeService.findAll(queryFilters, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of Employee instances matching the optional query (q) request param. If there is no query provided, it returns all the instances. Pagination & Sorting parameters such as page& size, sort can be sent as request parameters. The sort value should be a comma separated list of field names & optional sort order to sort the data on. eg: field1 asc, field2 desc etc ")
    @RequestMapping(method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Employee> findEmployees(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering Employees list");
        return employeeService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of Employee instances matching the optional query (q) request param. This API should be used only if the query string is too big to fit in GET request with request param. The request has to made in application/x-www-form-urlencoded format.")
    @RequestMapping(value="/filter", method = RequestMethod.POST, consumes= "application/x-www-form-urlencoded")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Employee> filterEmployees(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering Employees list");
        return employeeService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns downloadable file for the data matching the optional query (q) request param. If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
    @RequestMapping(value = "/export/{exportType}", method = {RequestMethod.GET,  RequestMethod.POST}, produces = "application/octet-stream")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Downloadable exportEmployees(@PathVariable("exportType") ExportType exportType, @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
         return employeeService.export(exportType, query, pageable);
    }

	@ApiOperation(value = "Returns the total count of Employee instances matching the optional query (q) request param. If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
	@RequestMapping(value = "/count", method = {RequestMethod.GET, RequestMethod.POST})
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	public Long countEmployees( @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query) {
		LOGGER.debug("counting Employees");
		return employeeService.count(query);
	}

    @ApiOperation(value = "Returns aggregated result with given aggregation info")
	@RequestMapping(value = "/aggregations", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	public Page<Map<String, Object>> getEmployeeAggregatedValues(@RequestBody AggregationInfo aggregationInfo, Pageable pageable) {
        LOGGER.debug("Fetching aggregated results for {}", aggregationInfo);
        return employeeService.getAggregatedValues(aggregationInfo, pageable);
    }

    @RequestMapping(value="/{id:.+}/vacations", method=RequestMethod.GET)
    @ApiOperation(value = "Gets the vacations instance associated with the given id.")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Vacation> findAssociatedVacations(@PathVariable("id") Integer id, Pageable pageable) {

        LOGGER.debug("Fetching all associated vacations");
        return employeeService.findAssociatedVacations(id, pageable);
    }

    @RequestMapping(value="/{id:.+}/employeesForManagerId", method=RequestMethod.GET)
    @ApiOperation(value = "Gets the employeesForManagerId instance associated with the given id.")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Employee> findAssociatedEmployeesForManagerId(@PathVariable("id") Integer id, Pageable pageable) {

        LOGGER.debug("Fetching all associated employeesForManagerId");
        return employeeService.findAssociatedEmployeesForManagerId(id, pageable);
    }

    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service EmployeeService instance
	 */
	protected void setEmployeeService(EmployeeService service) {
		this.employeeService = service;
	}

}

