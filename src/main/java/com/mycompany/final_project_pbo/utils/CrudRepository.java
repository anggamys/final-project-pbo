/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.utils;

import java.util.ArrayList;

/**
 *
 * @author c0delb08
 * @param <T>
 */
public interface CrudRepository<T> {
    Response<T> save(T entity);

    Response<T> update(T entity);

    Response<T> findById(Integer id);

    Response<Boolean> deleteById(Integer id);

    Response<ArrayList<T>> findAll();
}
