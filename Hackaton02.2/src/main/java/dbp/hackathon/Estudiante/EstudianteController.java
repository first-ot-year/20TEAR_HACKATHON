package dbp.hackathon.Estudiante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @GetMapping("/hola")
    public ResponseEntity<String> hola() {
        return ResponseEntity.ok("Hola Mundo, dbp esta en la casa");
    }

    @PostMapping
    public ResponseEntity<Estudiante> save(@RequestBody Estudiante estudiante) {
        return ResponseEntity.ok(estudianteService.save(estudiante));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> findById(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        estudianteService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Iterable<Estudiante>> findAll() {
        return ResponseEntity.ok(estudianteService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> replaceEstudiante(@RequestBody Estudiante newEstudiante, @PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.replaceEstudiante(newEstudiante, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Estudiante> updateParcialEstudiante(@RequestBody Estudiante newEstudiante, @PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.updateParcialEstudiante(newEstudiante, id));
    }
}
