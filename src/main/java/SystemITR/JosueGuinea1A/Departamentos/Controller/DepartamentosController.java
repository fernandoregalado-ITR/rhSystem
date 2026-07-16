package SystemITR.JosueGuinea1A.Departamentos.Controller;

import SystemITR.JosueGuinea1A.Departamentos.DTO.DepartamentosDTO;
import SystemITR.JosueGuinea1A.Departamentos.Service.DepartamentosService;
import SystemITR.JosueGuinea1A.Response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/departamento")
@Slf4j
@CrossOrigin
//CroosOrigin es para que recibe peticiones de todos lados


public class DepartamentosController {

    //Try catch poner siempre ya que es buena practica

    private final DepartamentosService service;

    public DepartamentosController(DepartamentosService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartamentosDTO>> nuevoDepartamento(@Valid @RequestBody DepartamentosDTO json) {
        //RequestBody lo usamos cuando llege un JSON con la peticion

        try {
            //Creamos un objeto DTO porque el service.insertarDatos retornará un objeto de tipo DepartamentosDTO
            DepartamentosDTO objDTO = service.insertarDatos(json);
            if (objDTO == null) {
                ApiResponse respuesta = new ApiResponse(false, "No se pudo completar el proceso de inserción", json);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }
            ApiResponse respuesta = new ApiResponse(true, "Dato ingresado exitosamente", objDTO);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuesta = new ApiResponse<>(false, "Error critico" + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartamentosDTO>>> obtenerDepartamentos() {
        try {
            List<DepartamentosDTO> listaDTO = service.listarTodos();
            if (listaDTO != null) {
                ApiResponse<List<DepartamentosDTO>> respuestaExistosa = new ApiResponse<>(true, "Proceso completado", listaDTO);
                return ResponseEntity.ok(respuestaExistosa);
            }
            ApiResponse<List<DepartamentosDTO>> respuestaNoData = new ApiResponse<>(true, "No hay datos por mostrar", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoData);
        } catch (Exception e) {
            log.info("El proceso presento un fallo inseperado, consulte con el administrados");
            e.printStackTrace();
            ApiResponse<List<DepartamentosDTO>> response = new ApiResponse<>(false, "El proceso no se pudo completar", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //Es para cuando viene por url
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtenerDepartamentosPorId(@PathVariable Long id) {
        try {
            DepartamentosDTO dto = service.buscarDepartamentos(id);
            if (dto != null) {
                //Armar la repsuesta utilizando ApiResponse
                log.info("Se obtuvieron los datos del servidor: " + dto);
                ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(true, "Dato encontrado", dto);
                return ResponseEntity.ok(respuestaExitosa);
            }
            //Estas dos lineas solo se van a ejecutar si solo la busqueda no enuentra
            ApiResponse<DepartamentosDTO> noEncontrado = new ApiResponse<>(
                    false, "Datos no encontrados", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        } catch (Exception e) {
            log.info("No hay departamentos registrados");
            //Sirve para encontrar el lugar exacto donde esta el error
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false, "No se pudo completar la bisqueda de ID: " + id, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarDepartamento(@PathVariable Long id) {
        try {
            boolean respuesta = service.eliminarInfo(id);
            if (respuesta) {

                ApiResponse<Void> respuestaExitosa = new ApiResponse<>(
                        true, "Dato con ID: " + id + " eliminado exitosamente", null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuestaExitosa);
            }
            ApiResponse<Void> respuestaNoRealizado = new ApiResponse<>(
                    false, "El proceso de eliminación no se pudo completar  debido a que nose encontro ningun departamo con ese id", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoRealizado);
        } catch (Exception e) {
            //log es un mensaje qu queda resgistradon nen el historial del servidor
            log.error("Error critico, consulte con el administrador");
            e.printStackTrace();
            ApiResponse<Void> respuestaError = new ApiResponse<>(
                    false, "Error critico, al adimistrador para solucionar el problema  " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartamentosDTO>> actualizarData
            (@PathVariable Long id, @Valid @RequestBody DepartamentosDTO dto) {
        try {
            DepartamentosDTO data = service.actualzar(id, dto);
            if (data != null) {
                log.info("Departamento con ID: " + id + " Ha sido actualizado.");
                ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(false, "Departamento" +
                        "con ID: " + id + "ha d");
            }
            log.warn("No se pudo completar la actulizacion del departamento con ID: " + id);
            ApiResponse<DepartamentosDTO> respuestaNoCompletada = new ApiResponse<>(false, "No se" +
                    "pudo completar la actualizacion del departamento con ID: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaNoCompletada);
        } catch (Exception e) {
            //log es un mensaje qu queda resgistradon nen el historial del servidor
            log.error("Error critico, al actualizar el departamento con ID: " + id);
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(
                    false, "Error critico, al actualizar el departamento  " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @GetMapping("/abreviatura/{abreviatura}")
    public ResponseEntity<ApiResponse<DepartamentosDTO>> buscarPorAbreviatura(@PathVariable String abreviatura){
        try {
            DepartamentosDTO data = service.buscaAbreviatura (abreviatura);
            if (data != null){
                log.info("Departamento encontrado con abreviatura: " + abreviatura);
                ApiResponse<DepartamentosDTO> respuestaExito = new ApiResponse<>
                        ( false, "Departamento encontrado con abreviatura: " + abreviatura, data);
                return ResponseEntity.ok(respuestaExito);
            }
            log.warn("Departamento no encontrado: " + abreviatura);
            ApiResponse<DepartamentosDTO> respuestaNoEncontrada = new ApiResponse<>(
                    false, "Departamento no encontrado: " + abreviatura);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoEncontrada);
        }catch (Exception e){
            log.error("Error critico, consulte con el administrador" + abreviatura);
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(
                    false, "Error critico, al obtener el departamento con ID  " + abreviatura);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }
}

