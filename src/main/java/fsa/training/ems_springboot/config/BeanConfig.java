package fsa.training.ems_springboot.config;

import fsa.training.ems_springboot.enums.EmployeeLevel;
import fsa.training.ems_springboot.model.entity.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


@Configuration
public class BeanConfig {

    @Bean
    public List<EmployeeLevel> managerLevels() {
        return Arrays.asList(EmployeeLevel.EXPERT, EmployeeLevel.SENIOR);
    }

    //        private final List<EmployeeLevel> mentorLevels; // LinkedList of JUNIOR , SENIOR
    @Bean
    public List<EmployeeLevel> mentorLevels() {
        List<EmployeeLevel> employeeLevels = new LinkedList<>();
        employeeLevels.add(EmployeeLevel.JUNIOR);
        employeeLevels.add(EmployeeLevel.SENIOR);
        return employeeLevels;
    }

}
