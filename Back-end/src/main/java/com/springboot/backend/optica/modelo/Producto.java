package com.springboot.backend.optica.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@JsonIgnoreProperties(value={"productos","hibernateLazyInitializer","handler"},allowSetters = true)
	@ManyToOne
	@JoinTable(
	        name = "producto_marca",
	        joinColumns = @JoinColumn(name = "producto_id"),
	        inverseJoinColumns = @JoinColumn(name = "marca_id")
	)
	private Marca marca;
	
	@Column(length = 50)
	private String modelo;
	
	@Column(length = 20)
	private Boolean stock;
	
	@Column
	private String descripcion;
	
	@NotNull
	@Column()
	private float precio;
	
	@Column(length = 20)
	private String color;
	
	@Column(length = 20)
	private String genero;
	
	@JsonIgnoreProperties(value={"productos","hibernateLazyInitializer","handler"},allowSetters = true)
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
	        name = "producto_categoria",
	        joinColumns = @JoinColumn(name = "producto_id"),
	        inverseJoinColumns = @JoinColumn(name = "categoria_id")
	)
	private List<Categoria> categorias;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false,name="creado_en")
	private Date creadoEn;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false,name="ultima_actualizacion")
	private Date ultimaActualizacion;
	
	private String foto;
	
	private static final long serialVersionUID = 1L;

}
