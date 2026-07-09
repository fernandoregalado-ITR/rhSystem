package SystemITR.JosueGuinea1A.Departamentos.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartamentosDTO {

    private Long id;
    @NotBlank
    private String nombreDepto;
    @NotBlank
    private String abreviatura;
    @NotBlank
    private String ubicacion;
}
