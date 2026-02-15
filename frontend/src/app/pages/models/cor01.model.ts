export class Cor01 {

  public id?: number
  public referencia?: string
  public codigoCor?: string
  public nomeCor?: string
  public venda?: number
  public venda10Dias?: number
  public estoque?: number

  // novos campos
  public vendaEcommerce?: number; // 4 digitos
  public venda10DiasEcommerce?: number; // 4 digitos
  public estoqueEcommerce?: number; // 4 digitos
  // fim novos campos

  public indice?: number
  public iop?: number
  public classe?: string
}
