package com.springsource.samples.customer;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.catalina.loader.WebappClassLoaderBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.security.ProtectionDomain;


/**
 * Dedicated controller for machine access.
 * 
 * @author Oliver Gierke
 */
@Controller
@Slf4j
@RequestMapping("/rest/customers")
class CustomerRestController {

    private final CustomerRepository repository;


    @Autowired
    public CustomerRestController(CustomerRepository repository) {

        Assert.notNull(repository);
        this.repository = repository;
    }


    @RequestMapping(method = GET)
    @ResponseBody
    public Customers showCustomers(Model model) {
        log.error(this.getClass().getClassLoader().toString());
        log.error(this.getClass().getClassLoader().getClass().getCanonicalName());
        if(this.getClass().getProtectionDomain() != null) {
            ProtectionDomain pd = this.getClass().getProtectionDomain();
            log.error(pd.toString());
            log.error(pd.getClassLoader().toString());
            log.error(pd.getClassLoader().getClass().getCanonicalName());
            if(WebappClassLoaderBase.class.isAssignableFrom(pd.getClassLoader().getClass())) {
                WebappClassLoaderBase cl = (WebappClassLoaderBase) pd.getClassLoader();
                log.error(cl.getResources().toString());
            }
        }
        return new Customers(repository.findCustomers());
    }


    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public void createCustomer(@ModelAttribute Customer customer,
            HttpServletResponse response) {

        repository.save(customer);
        response.setHeader("Location", String.format("/rest/customers/%s",
                customer.getNumber()));
    }


    @RequestMapping(value = "/{id}", method = GET)
    @ResponseBody
    public Customer showCustomer(@PathVariable CustomerNumber id) {

        return repository.findBy(id);
    }


    @RequestMapping(value = "/{id}", method = PUT)
    @ResponseStatus(OK)
    public void updateCustomer(@RequestBody Customer customer) {

        repository.save(customer);
    }


    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseStatus(OK)
    public void deleteCustomer(@PathVariable CustomerNumber id) {

        repository.delete(id);
    }
}
