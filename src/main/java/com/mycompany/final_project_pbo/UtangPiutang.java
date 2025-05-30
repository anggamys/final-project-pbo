/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo;

import com.mycompany.final_project_pbo.Utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c0delb08
 */
public class UtangPiutang implements BaseEntity, CrudRepository<UtangPiutang>{
    private Integer id;
    private String namaPihak;
    private Double nominal;
    private Date jatuhTempo;
    private String status;
    
    private static final Logger LOGGER = Logger.getLogger(UtangPiutang.class.getName());
    
    public UtangPiutang() {}
    public UtangPiutang(Integer id, String namaPihak, Double nominal, Date jatuhTempo, String status) {
        this.id = id;
        this.namaPihak = namaPihak;
        this.nominal = nominal;
        this.jatuhTempo = jatuhTempo;
        this.status = status;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaPihak() {
        return namaPihak;
    }

    public void setNamaPihak(String namaPihak) {
        this.namaPihak = namaPihak;
    }

    public Double getNominal() {
        return nominal;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public Date getJatuhTempo() {
        return jatuhTempo;
    }

    public void setJatuhTempo(Date jatuhTempo) {
        this.jatuhTempo = jatuhTempo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Response save() {
        String sql = "INSERT INTO UtangPiutang (namaPihak, nominal, jatuhTempo, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, namaPihak);
            ps.setDouble(2, nominal);
            ps.setDate(3, new java.sql.Date(jatuhTempo.getTime()));
            ps.setString(4, status);
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
            
            return Response.success("Hutang piutang added succesfully", this);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save Hutang piutang", e);
            return Response.failure("Error saving Hutang piutang: " + e.getMessage());
        }
    }

    @Override
    public Response update() {
        String sql = "UPDATE UtangPiutang SET namaPihak = ?, nominal = ?, jatuhTempo = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, namaPihak);
            ps.setDouble(2, nominal);
            ps.setDate(3, new java.sql.Date(jatuhTempo.getTime()));
            ps.setString(4, status);
            ps.setInt(5, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Hutang piutang updated successfully", this);
            } else {
                return Response.failure("No data found to update");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update Hutang piutang", e);
            return Response.failure("Error updating Hutang piutang: " + e.getMessage());
        }
    }

    @Override
    public Response<UtangPiutang> findById(int id) {
        String sql = "SELECT * FROM UtangPiutang WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UtangPiutang data = new UtangPiutang(
                            rs.getInt("id"),
                            rs.getString("namaPihak"),
                            rs.getDouble("nominal"),
                            rs.getDate("jatuhTempo"),
                            rs.getString("status")
                    );
                    return Response.success("Data found", data);
                } else {
                    return Response.failure("Data not found");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve data", e);
            return Response.failure("Error retrieving data: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> deleteById(int id) {
        String sql = "DELETE FROM UtangPiutang WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Data deleted", true);
            } else {
                return Response.failure("No data found to delete");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete data", e);
            return Response.failure("Error deleting data: " + e.getMessage());
        }
    }

    @Override
    public Response<ArrayList<UtangPiutang>> findAll() {
        String sql = "SELECT * FROM UtangPiutang";
        ArrayList<UtangPiutang> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UtangPiutang data = new UtangPiutang(
                        rs.getInt("id"),
                        rs.getString("namaPihak"),
                        rs.getDouble("nominal"),
                        rs.getDate("jatuhTempo"),
                        rs.getString("status")
                );
                list.add(data);
            }

            return Response.success("Data loaded", list);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load data", e);
            return Response.failure("Error loading data: " + e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return "UtangPiutang{" +
                "id=" + id +
                ", namaPihak='" + namaPihak + '\'' +
                ", nominal=" + nominal +
                ", jatuhTempo=" + jatuhTempo +
                ", status='" + status + '\'' +
                '}';
    }
}
