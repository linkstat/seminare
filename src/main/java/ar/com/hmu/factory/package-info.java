/**
 * Paquete Factory: contiene clases y métodos responsables de la creación de instancias complejas de objetos según su tipo específico.
 * <p>
 * El objetivo principal de este paquete es centralizar la lógica de instanciación, respetando el principio de
 * responsabilidad única (SRP). Al agrupar la lógica de creación en un solo lugar, se facilita el mantenimiento,
 * la reutilización del código y la separación clara de responsabilidades dentro de la aplicación.
 * <p>
 * Este paquete incluye la clase {@code UsuarioFactory}, encargada de la creación de diferentes tipos de usuarios,
 * tales como {@code Empleado}, {@code JefaturaDeServicio}, {@code OficinaDePersonal}, y {@code Direccion},
 * a partir de los datos obtenidos de la base de datos. Al hacerlo, se oculta la complejidad de la lógica de
 * instanciación y se permite un manejo más sencillo y flexible de las entidades según su tipo específico.
 */
package ar.com.hmu.factory;
