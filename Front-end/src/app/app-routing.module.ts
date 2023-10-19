import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/user/login.component';
import { PaginaPrincipalComponent } from './components/pagina-principal/pagina-principal.component';
import { PaginaSobreNosotrosComponent } from './components/pagina-sobre-nosotros/pagina-sobre-nosotros.component';
import { PaginaFiltrarProductoComponent } from './components/pagina-filtrar-producto/pagina-filtrar-producto.component';
import { PaginaCristaleriaComponent } from './components/pagina-cristaleria/pagina-cristaleria.component';
import { PaginaCrearEditarProductoComponent } from './components/pagina-crear-editar-producto/pagina-crear-editar-producto.component';
import { PaginaGestionarCategoriasComponent } from './components/pagina-gestionar-categorias/pagina-gestionar-categorias.component';
import { PaginaVisitanosComponent } from './components/pagina-visitanos/pagina-visitanos.component';
import { PaginaGestionarMarcasComponent } from './components/pagina-gestionar-marcas/pagina-gestionar-marcas.component';
import { PaginaPoliticaEnvioComponent } from './components/pagina-politica-envio/pagina-politica-envio.component';
import { PaginaPoliticaGarantiaComponent } from './components/pagina-politica-garantia/pagina-politica-garantia.component';
import { PaginaLimpiezaComponent } from './components/pagina-limpieza/pagina-limpieza.component';

const routes: Routes = [
  {path:'', redirectTo:'/inicio', pathMatch:'full'},
  {path:'inicio',component: PaginaPrincipalComponent},
  {path:'inicio/page/:genero/:marca/:categoria/:page',component: PaginaPrincipalComponent},
  {path:'sobreNosotros',component: PaginaSobreNosotrosComponent},
  {path:'productos/page/:genero/:marca/:categoria/:page',component: PaginaFiltrarProductoComponent},
  {path:'cristaleria',component: PaginaCristaleriaComponent},
  {path:'limpieza',component: PaginaLimpiezaComponent},
  {path:'politicaEnvio',component: PaginaPoliticaEnvioComponent},
  {path:'politicaGarantia',component: PaginaPoliticaGarantiaComponent},
  {path:'crearProducto',component: PaginaCrearEditarProductoComponent},
  {path:'editarProducto/:id',component: PaginaCrearEditarProductoComponent},
  {path:'adminitrarCategoria',component: PaginaGestionarCategoriasComponent},
  {path:'adminitrarMarca',component: PaginaGestionarMarcasComponent},
  {path:'visitanos',component: PaginaVisitanosComponent},
  {path:'login',component: LoginComponent},
  { path: '**', redirectTo: '/inicio' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
