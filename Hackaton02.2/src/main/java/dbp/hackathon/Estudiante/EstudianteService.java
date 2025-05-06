package dbp.hackathon.Estudiante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    Estudiante save(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    Estudiante findById(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    void deleteById(Long id) {
        estudianteRepository.deleteById(id);
    }

    Iterable<Estudiante> findAll() {
        return estudianteRepository.findAll();
    }

    Estudiante replaceEstudiante(Estudiante newEstudiante, Long id) {
        return estudianteRepository.findById(id)
                .map(estudiante -> {
                    estudiante.setName(newEstudiante.getName());
                    estudiante.setEmail(newEstudiante.getEmail());
                    estudiante.setStudentCode(newEstudiante.getStudentCode());
                    return estudianteRepository.save(estudiante);
                })
                .orElseGet(() -> {
                    return estudianteRepository.save(newEstudiante);
                });
    }


    Estudiante updateParcialEstudiante(Estudiante newEstudiante, Long id) {
        return estudianteRepository.findById(id)
                .map(estudiante -> {
                    if (newEstudiante.getName() != null) {
                        estudiante.setName(newEstudiante.getName());
                    }
                    if (newEstudiante.getEmail() != null) {
                        estudiante.setEmail(newEstudiante.getEmail());
                    }
                    if (newEstudiante.getStudentCode() != null) {
                        estudiante.setStudentCode(newEstudiante.getStudentCode());
                    }
                    return estudianteRepository.save(estudiante);
        })
        .orElseGet(() -> {
            newEstudiante.setId(id);
            return estudianteRepository.save(newEstudiante);
        });
    }
}
