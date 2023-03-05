package charlie.springbootmvc.restervice.services;

import charlie.springbootmvc.restervice.entities.Employee;
import charlie.springbootmvc.restervice.mappers.EmployeeMapper;
import charlie.springbootmvc.restervice.model.EmployeeDTO;
import charlie.springbootmvc.restervice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class EmployeeServiceJpa implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    @Override
    public List<EmployeeDTO> listEmployees() {
        return this.employeeRepository.findAll()
                .stream()
                .map(employeeMapper::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeDTO> getById(UUID id) {

        return employeeRepository.findById(id).map(employeeMapper::employeeToEmployeeDTO);
    }

    @Override
    public EmployeeDTO newEmployee(EmployeeDTO employeeDTO) {

        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        return employeeMapper.employeeToEmployeeDTO(employeeRepository.save(employee));
    }

    @Override
    public Optional<EmployeeDTO> updateById(UUID id, EmployeeDTO employeeDTO) {
        AtomicReference<Optional<EmployeeDTO>> atomicReference = new AtomicReference<>();
        Optional<Employee> existing = employeeRepository.findById(id);
        existing.ifPresentOrElse((employee) -> {
            Employee newEmployee = employeeMapper.employeeDTOToEmployee(employeeDTO);
            newEmployee.setId(id);
            atomicReference.set(Optional.of(
                    employeeMapper.employeeToEmployeeDTO(employeeRepository.save(newEmployee))
            ));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public EmployeeDTO patchEmployee(UUID id, EmployeeDTO employee) {
        return null;
    }
}
