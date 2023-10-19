import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductoService } from './services/producto.service';
import { CategoriaService } from './services/categoria.service';
import { MarcaService } from './services/marca.service';

import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './components/user/login.component';
import { HeaderComponent } from './components/header/header.component';
import { AuthToken } from './services/interceptors/auth.interceptor';
import { TokenInterceptor } from './services/interceptors/token.interceptor';
import { FooterComponent } from './components/footer/footer.component';
import { ListarProductoComponent } from './components/listar-producto/listar-producto.component';
import { PaginaPrincipalComponent } from './components/pagina-principal/pagina-principal.component';
import { PaginatorComponent } from './components/paginator/paginator.component';
import { PaginaSobreNosotrosComponent } from './components/pagina-sobre-nosotros/pagina-sobre-nosotros.component';
import { PaginaCristaleriaComponent } from './components/pagina-cristaleria/pagina-cristaleria.component';
import { PaginaFiltrarProductoComponent } from './components/pagina-filtrar-producto/pagina-filtrar-producto.component';
import { PaginaCrearEditarProductoComponent } from './components/pagina-crear-editar-producto/pagina-crear-editar-producto.component';
import { PaginaGestionarCategoriasComponent } from './components/pagina-gestionar-categorias/pagina-gestionar-categorias.component';
import { WhatsappComponent } from './components/whatsapp/whatsapp.component';
import { PaginaVisitanosComponent } from './components/pagina-visitanos/pagina-visitanos.component';
import { PaginaGestionarMarcasComponent } from './components/pagina-gestionar-marcas/pagina-gestionar-marcas.component';
import { PaginaLimpiezaComponent } from './components/pagina-limpieza/pagina-limpieza.component';
import { PaginaPoliticaEnvioComponent } from './components/pagina-politica-envio/pagina-politica-envio.component';
import { PaginaPoliticaGarantiaComponent } from './components/pagina-politica-garantia/pagina-politica-garantia.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HeaderComponent,
    FooterComponent,
    ListarProductoComponent,
    PaginaPrincipalComponent,
    PaginatorComponent,
    PaginaSobreNosotrosComponent,
    PaginaCristaleriaComponent,
    PaginaFiltrarProductoComponent,
    PaginaCrearEditarProductoComponent,
    PaginaGestionarCategoriasComponent,
    WhatsappComponent,
    PaginaVisitanosComponent,
    PaginaGestionarMarcasComponent,
    PaginaLimpiezaComponent,
    PaginaPoliticaEnvioComponent,
    PaginaPoliticaGarantiaComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    ProductoService,
    CategoriaService,
    MarcaService,
    {provide:HTTP_INTERCEPTORS,useClass:TokenInterceptor,multi:true},
    {provide:HTTP_INTERCEPTORS,useClass:AuthToken,multi:true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
