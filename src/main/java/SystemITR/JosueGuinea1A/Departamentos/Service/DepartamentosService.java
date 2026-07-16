package SystemITR.JosueGuinea1A.Departamentos.Service;

import SystemITR.JosueGuinea1A.Departamentos.DTO.DepartamentosDTO;
import SystemITR.JosueGuinea1A.Departamentos.Entity.DepartamentosEntity;
import SystemITR.JosueGuinea1A.Departamentos.Repository.DepartamentosRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartamentosService {

    @Autowired
    private DepartamentosRepository repo;

    public DepartamentosDTO insertarDatos(@Valid DepartamentosDTO jsonData){
        //Validando que el objeto json no sea nulo
        if (jsonData == null){
            throw new IllegalArgumentException("El departamento no puede ser nulo");
        }

        //Intentar hacer la inserción
        try{
            //1. Convertir el objeto DTO a Entity
            System.out.println("Bandera 1: Ejecución antes de conversión");
            DepartamentosEntity entity = convertirAEntity(jsonData);
            System.out.println("Bandera 2: Ejecución después de conversión y antes de guardar");
            //Aqui hicimos la inserción a la base de datos
            DepartamentosEntity entitySave = repo.save(entity);
            System.out.println("Bandera 3: Ejecución después de guardar");
            //Una vez que los datos han sido ingresados la respuesta debe convertirse a DTO nuevamente
            //ahora de Entity -> DTO
            return convertirADTO(entitySave);
        }catch (Exception e){
            log.error("Error al ingresar la información del departamento" + e.getMessage());
            throw new RuntimeException("Error al registrar el departamento");
        }
    }

    private DepartamentosDTO convertirADTO(DepartamentosEntity entitySave) {
        DepartamentosDTO objDTO = new DepartamentosDTO();
        objDTO.setId(entitySave.getId());
        objDTO.setNombreDepto(entitySave.getNombreDepto());
        objDTO.setAbreviatura(entitySave.getAbreviatura());
        objDTO.setUbicacion(entitySave.getUbicacion());
        return objDTO;
    }

    private DepartamentosEntity convertirAEntity(@Valid DepartamentosDTO jsonData) {
        //Creando objeto que vamos a retornar
        DepartamentosEntity objEntity = new DepartamentosEntity();
        objEntity.setNombreDepto(jsonData.getNombreDepto());
        objEntity.setAbreviatura(jsonData.getAbreviatura());
        objEntity.setUbicacion(jsonData.getUbicacion());
        return objEntity;
    }

    public List<DepartamentosDTO> listarTodos() {
        List<DepartamentosEntity> entidades = repo.findAll();
        return entidades.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public DepartamentosDTO buscarDepartamentos(Long id) {
        Optional<DepartamentosEntity> entidadOpcional = repo.findById(id);
        //Validar si el ID existe, en caso fuera cierto convertimos el dato a DTO de lo contrario retornamos a null
        return entidadOpcional.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarInfo(Long id) {
        if (repo.existsById(id)){
            //Paso 2
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public DepartamentosDTO actualzar(Long id, @Valid DepartamentosDTO dto) {
        try {
            //1. Buscar si el departamento realmente existe por su id
            Optional<DepartamentosEntity> entidadOpcional = repo.findById(id);
            //2. Verificar si el objeto contene valores (utilizando if)
            if (entidadOpcional.isPresent()){
                //2.1 Creamos un objeto de tipo entidad
                DepartamentosEntity entidad = entidadOpcional.get();
                //2.2 Convertir y asignar los dtos (nuevos valores) a entidad
                entidad.setNombreDepto(dto.getNombreDepto());
                entidad.setAbreviatura(dto.getAbreviatura());
                entidad.setUbicacion(dto.getUbicacion());
                //2.3 Actualizar los datos en la base de datos
                DepartamentosEntity datosGuardados = repo.save(entidad);
                //2.4 Retornar la data convertida a DTO de forma previa.
                return convertirADTO(datosGuardados);
            }
            //3. Retornar null
            return null;
        }catch (Exception e){
            log.error("Oops, ocurrio un error al procesar la informacion");
            return null;
        }
    }

    public DepartamentosDTO buscaAbreviatura(String abreviatura) {
        try {
            Optional<DepartamentosEntity> registro = repo.findByAbreviatura(abreviatura);
            if (registro.isPresent()){
                return convertirADTO(registro.get());
            }
            log.warn("No existe ningun departamento con esa abreviatura: " + abreviatura);
            return null;
        }catch (Exception e){
            log.error("Ocurrio un error durante el proceso");
            return null;
        }
    }
}
