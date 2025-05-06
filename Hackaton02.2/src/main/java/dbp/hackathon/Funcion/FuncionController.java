package dbp.hackathon.Funcion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/funcion")
public class FuncionController {

    @Autowired
    private FuncionService funcionService;

    @PostMapping
    public ResponseEntity<Funcion> saveFuncion(@RequestBody Funcion funcion) {
        return ResponseEntity.ok(funcionService.saveFuncion(funcion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcion> getFuncionById(@PathVariable Long id) {
        return ResponseEntity.ok(funcionService.getFuncionById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuncion(@PathVariable Long id) {
        funcionService.deleteFuncion(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcion> replaceFuncion(@PathVariable Long id, @RequestBody Funcion funcion) {
        return ResponseEntity.ok(funcionService.replaceFuncion(id, funcion));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Funcion> updateFuncion(@PathVariable Long id, @RequestBody Funcion funcion) {
        return ResponseEntity.ok(funcionService.updateFuncion(id, funcion));
    }

    @GetMapping("/{id}/earnings")
    public ResponseEntity<Double> getTotalEarnings(@PathVariable Long id) {
        try {
            double earnings = funcionService.calculateTotalEarnings(id);
            return ResponseEntity.ok(earnings);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
