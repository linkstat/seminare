package ar.com.hmu.factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import ar.com.hmu.model.Domicilio;

public class DomicilioFactory {

    public static Domicilio createDomicilio(ResultSet rs) throws SQLException {
        return new Domicilio.Builder()
                .setId(UUID.fromString(rs.getString("id")))
                .setCalle(rs.getString("calle"))
                .setNumeracion(rs.getString("numeracion"))
                .setBarrio(rs.getString("barrio"))
                .setCiudad(rs.getString("ciudad"))
                .setLocalidad(rs.getString("localidad"))
                .setProvincia(rs.getString("provincia"))
                .build();
    }

}
