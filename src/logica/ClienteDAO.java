package logica;

import entidad.Cliente;
import entidad.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class ClienteDAO {
    
    private Conexion con = new Conexion();
    private Connection acceso;
    private PreparedStatement ps;
    private ResultSet rs;
    private String sql;
    
  
    public int registrarCliente(Cliente cli){
        int rpta = 0;
        sql = "INSERT INTO persona (cedula,nombre,apellido,telefono,direccion) VALUES(?,?,?,?,?)";
        String sql2 = "INSERT INTO cliente (idCliente) VALUES((SELECT idPersona FROM persona ORDER BY idpersona DESC LIMIT 1))";
        
        try {
            acceso = con.conectar();
            ps = acceso.prepareStatement(sql);
            ps.setString(1, cli.getCedula());
            ps.setString(2, cli.getNombre());
            ps.setString(3, cli.getApellido());
            ps.setString(4, cli.getTelefono());
            ps.setString(5, cli.getDireccion());
            
            if(ps.executeUpdate() != 0){
                PreparedStatement ps2 = acceso.prepareStatement(sql2);
                rpta =ps2.executeUpdate();
            }
            
            acceso.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }finally {
            try {
                acceso.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
                
        return rpta;
    }
    
    public Cliente consultarCliente(String cedula){
        
        sql = "SELECT * FROM persona JOIN cliente ON persona.idPersona = cliente.idCliente WHERE cedula like ?";
        Cliente c = new Cliente();
        try {
            acceso = con.conectar();
            ps = acceso.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();
            
            if(rs.next()){
                c.setIdPersona(rs.getInt(1));
                c.setCedula(rs.getString(2));
                c.setNombre(rs.getString(3));
                c.setApellido(rs.getString(4));
                c.setTelefono(rs.getString(5));
                c.setDireccion(rs.getString(6));
            }
            acceso.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }finally {
            try {
                acceso.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        return c;
    }
        
    public int actualizarCliente(Cliente cli){
        int rpta = 0;
        sql = "UPDATE persona SET nombre = ?, apellido = ?, telefono = ?, direccion = ? WHERE cedula like ?";
                
        try {
            acceso = con.conectar();
            ps = acceso.prepareStatement(sql);
            ps.setString(1, cli.getNombre());
            ps.setString(2, cli.getApellido());
            ps.setString(3, cli.getTelefono());
            ps.setString(4, cli.getDireccion());
            ps.setString(5, cli.getCedula());
            
            rpta = ps.executeUpdate();
            acceso.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }finally {
            try {
                acceso.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
                
        return rpta;
    }
    
    public DefaultTableModel visualizarCliente(String cedula, String apellido){
            
        DefaultTableModel modelo;
        String [] titulos = {"CÉDULA","NOMBRES","APELLIDOS","DIRECCIÓN","TELÉFONO"};
        String [] registro = new String [5];
        
        modelo = new DefaultTableModel(null, titulos){
            
            @Override
            public boolean isCellEditable(int filas, int columnas){
                if(columnas== 1){
                    return true;
                }else{
                    return false;
                }
            }
        };
                        
        sql = "SELECT cedula, nombre, apellido, direccion, telefono FROM persona WHERE cedula like ? and apellido like ? ORDER BY idPersona";
              
        try{
            acceso = con.conectar();
            ps = acceso.prepareStatement(sql);
            ps.setString(1, "%" + cedula + "%");
            ps.setString(2, "%" + apellido + "%");
            rs = ps.executeQuery();
            while(rs.next()){
                registro[0] = rs.getString(1);
                registro[1] = rs.getString(2);
                registro[2] = rs.getString(3);
                registro[3] = rs.getString(4);
                registro[4] = rs.getString(5);
                               
                modelo.addRow(registro);
                
            }
            acceso.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }finally {
            try {
                acceso.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return modelo;
    }
}
