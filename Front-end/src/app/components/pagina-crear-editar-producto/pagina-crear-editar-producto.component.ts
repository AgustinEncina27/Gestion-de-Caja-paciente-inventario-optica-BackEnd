import { HttpEventType } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Categoria } from 'src/app/models/categoria';
import { Marca } from 'src/app/models/marca';
import { Producto } from 'src/app/models/producto';
import { CategoriaService } from 'src/app/services/categoria.service';
import { MarcaService } from 'src/app/services/marca.service';
import { ProductoService } from 'src/app/services/producto.service';
import Swal from 'sweetalert2';
import { URL_BACKEND } from 'src/app/config/config';

@Component({
  selector: 'app-pagina-crear-producto',
  templateUrl: './pagina-crear-editar-producto.component.html',
  styleUrls: ['./pagina-crear-editar-producto.component.css']
})
export class PaginaCrearEditarProductoComponent implements OnInit {
  categorias:Categoria[]=[];
  marcas:Marca[]=[];
  marca:Marca=new Marca();
  producto: Producto= new Producto();
  titulo:String ='Crear Producto';
  private fotoSeleccionada: File | null = null;
  selectedCategoriesCheckbox: Categoria[] = [];
  newNameCategoryInput: string = '';
  stock:boolean=false;
  fotoEstaSeleccionada: boolean = false;
  genero:string='';
  URL_BACKEND: string=URL_BACKEND;
  

  constructor(private productoService: ProductoService,
     private activatedRoute: ActivatedRoute,
     private categoriaService:CategoriaService,
     private marcaService:MarcaService,
     private router:Router){}
     progreso: number=0;


  ngOnInit(): void {
    this.cargarProducto()
    this.cargarCategorias()
    this.cargarMarcas()    
  }

  cargarCategorias(){
    this.categoriaService.getCategories().subscribe(
      categorias=>{this.categorias=categorias
      }
    )
  }

  cargarMarcas(){
    this.marcaService.getMarcas().subscribe(
      marcas=>{this.marcas=marcas
      }
    )
  }

  cargarProducto(){
    this.activatedRoute.paramMap.subscribe(params=>{
      this.titulo='Crear Producto'
      this.producto= new Producto();
      let id: number = +params.get('id')!;
      if(id){
        this.titulo='Editar Producto';
        this.productoService.getProducto(id).subscribe(producto=>{
          this.producto=producto;
          this.marca=this.producto.marca
          this.genero= this.producto.genero
          this.selectedCategoriesCheckbox = producto.categorias; 
        })
      }
      

    })
  }

  seleccionarFoto(event: Event) {
    const inputElement = (event.target as HTMLInputElement);
    const files = inputElement?.files;
  
    if (files && files.length > 0) {
      this.fotoEstaSeleccionada = true;
      this.fotoSeleccionada = files[0];
      this.progreso=0;
    } else {
      this.fotoEstaSeleccionada = false;
    }

    if(this.fotoSeleccionada && this.fotoSeleccionada.type && this.fotoSeleccionada.type.indexOf('image') < 0){
      Swal.fire("Error al seleccionar la imagen","El archivo debe ser del tipo imagen","error")
      this.fotoSeleccionada= null;
    }
  }

  subirFoto(){
    if (this.fotoSeleccionada !== null) {
      this.productoService.subirFoto(this.fotoSeleccionada, this.producto.id).subscribe(
          event=> {
            if (event.type === HttpEventType.UploadProgress && event.total !== undefined) {
              this.progreso = Math.round((event.loaded / event.total) * 100);
            }else if(event.type === HttpEventType.Response){
              let response: any = event.body;
              this.producto=response.producto as Producto;
              Swal.fire("Producto creado","El producto ha sido guardado con éxito!","success");
              this.router.navigate(['/inicio']);
            }           
          }
      );
    } else {
        Swal.fire("Error","Por favor ingrese una foto","error")
    }
  }

  crearProducto(){
    if (!this.fotoSeleccionada) {
      Swal.fire("Error","Por favor ingrese una foto","error")
      return; // Salir de la función si no se seleccionó una foto.
    }
    this.producto.categorias=this.selectedCategoriesCheckbox
    this.producto.stock=this.stock
    this.producto.marca=this.marca
    this.producto.genero=this.genero;
    this.producto.creadoEn= new Date()
    this.producto.ultimaActualizacion= new Date()
    this.productoService.createProducto(this.producto).subscribe(
      response=>{
        this.producto=response
        this.subirFoto()
      }
    )
  }

  editarProducto(){
    this.producto.categorias=this.selectedCategoriesCheckbox;
    this.producto.stock=this.stock;
    this.producto.marca=this.marca;
    this.producto.genero=this.genero;
    this.producto.ultimaActualizacion= new Date();
    this.productoService.updateProducto(this.producto).subscribe(
      response=>{
        this.producto=response
        if(this.producto.foto==null || this.fotoEstaSeleccionada){
          this.subirFoto()
        }else{
          Swal.fire("Producto Editado","Se ha editado con éxito!","success");
          this.router.navigate(['/inicio']);
        }
      }
    )
  }

  selecionStock(stock:number){
    if(stock==1){
      this.stock=true
    }else{
      this.stock=false
    }
  }

  selecionMarca(marca:Marca){
    this.marca=marca;
  }

  selecionGenero(genero:string){
    this.genero=genero;
  }

  //CATEGORIAS
  isCategorySelectedInCheckBox(categoria: Categoria): boolean {
    return this.selectedCategoriesCheckbox.findIndex(selectedCategoria => selectedCategoria.id === categoria.id) !== -1;
  }
  
  toggleCategorySelection(categoria: Categoria) {
    if (this.isCategorySelectedInCheckBox(categoria)) {
      this.selectedCategoriesCheckbox = this.selectedCategoriesCheckbox.filter(selectedCategory => selectedCategory.id !== categoria.id); 
    } else {
      this.selectedCategoriesCheckbox.push(categoria);
    }
  }

  estaSeleccionado(marca:Marca): boolean {
    
    if(marca.id==this.marca.id && this.marca!==undefined){
      return true
    }
    return false
  }

  generoSeleccionado(genero:string):boolean{
    if(this.genero==null ){
      return false
    }
    if(this.producto.genero!==undefined){
      if(this.producto.genero.includes(genero) && this.producto.genero!==undefined){
        return true
      }
    }
    return false
  }

}
