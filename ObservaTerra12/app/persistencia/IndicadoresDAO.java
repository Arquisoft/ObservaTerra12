package persistencia;

import java.util.List;

import model.Indicator;

public interface IndicadoresDAO {
	
	//Añadir un indicador
	public Indicator añadirIndicador(Indicator indicador);
	
	//Recuperar indicadores
	public Indicator leerIndicador(Long idIndicador);
	
	public Indicator leerIndicador(String nombreIndicador);
	
	//Listar todos los indicadores
	public List<Indicator> listarTodosLosIndicadores();
	
	//Eliminar indicador
	public void eliminarIndicador(Indicator indicador);
	
	//Actualizar indicador
	public void actualizarIndicador(Indicator indicador);

}
