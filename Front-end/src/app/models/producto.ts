import { Categoria } from "./categoria";
import { Marca } from "./marca";


export class Producto {
    id!: number;
    marca!: Marca;
    modelo!: string;
    stock!: boolean;
    descripcion!: string;
    precio!: number;
    color!: string;
    genero!:string;
    categorias!: Categoria[];
    creadoEn!: Date;
    ultimaActualizacion!:Date;
    foto!:string;
    showDetails:boolean=false;
}
