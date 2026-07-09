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

@Slf4j
@RestController
@RequestMapping("/api/departamento")
public class DepartamentosController {

    private final DepartamentosService service;

    public DepartamentosController(DepartamentosService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartamentosDTO>> nuevoDepartamento(@Valid @RequestBody DepartamentosDTO json){
        //RequestBody lo usamos cuando llege un JSON con la peticion

        try{
            //Creamos un objeto DTO porque el service.insertarDatos retornará un objeto de tipo DepartamentosDTO
            DepartamentosDTO objDTO = service.insertarDatos(json);
            if (objDTO == null){
                ApiResponse respuesta = new ApiResponse(false,"No se pudo completar el proceso de inserción", json);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }
            ApiResponse respuesta = new ApiResponse(true, "Dato ingresado exitosamente", objDTO);
            return ResponseEntity.ok(respuesta);
        }catch (Exception e){
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuesta = new ApiResponse<>(false, "Error critico"+ e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartamentosDTO>>> obtenerDepartamentos(){
        try{
            List<DepartamentosDTO> listaDTO = service.listarTodos();
            if (listaDTO != null){
                ApiResponse<List<DepartamentosDTO>> respuestaExistosa = new ApiResponse<>(true, "Proceso completado", listaDTO);
                return ResponseEntity.ok(respuestaExistosa);
            }
            ApiResponse<List<DepartamentosDTO>> respuestaNoData = new ApiResponse<>(true, "No hay datos por mostrar", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoData);
        }catch (Exception e){
            log.info("No hay departamentos registrados");
            e.printStackTrace();
            ApiResponse List<respuestaError = new ApiResponse<>(false, "No se pudo obtener datos: ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    //Es para cuando viene por url
    @GetMapping ("/{id}")
    public ResponseEntity<ApiResponse> obtenerDepartamentosPorId(@PathVariable Long id){
        try{
            DepartamentosDTO dto = service.buscarDepartamentos(id);
            if (dto != null){
                //Armar la repsuesta utilizando ApiResponse
                ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(true, "Dato encontrado", dto);
                return ResponseEntity.ok(respuestaExitosa);
            }
            //Estas dos lineas solo se van a ejecutar si solo la busqueda no enuentra
            ApiResponse<DepartamentosDTO> noEncontrado = new ApiResponse<>(false, "Datos no encontrados", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        }catch (Exception e){
            log.info("No hay departamentos registrados");
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false, "No se pudo completar la bisqueda de ID: "+ id, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }
}
