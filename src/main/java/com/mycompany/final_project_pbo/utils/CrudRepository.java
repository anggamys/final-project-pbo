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
    Response<T> save(T entity, Integer userId);

    Response<T> update(T entity, Integer userId);

    Response<T> findById(Integer id, Integer userId);

    Response<Boolean> deleteById(Integer id, Integer userId);

    Response<ArrayList<T>> findAll(Integer userId);
}
