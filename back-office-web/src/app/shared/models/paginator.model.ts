/**
 * Modelo genérico de respuesta paginada del backend (Spring Data Page<T>).
 * El backend devuelve este formato en todos los endpoints paginados.
 */
export interface Paginator<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;       // página actual (0-based)
  size: number;         // tamaño de página
  first: boolean;
  last: boolean;
  empty: boolean;
}

/**
 * Crea un paginador vacío para manejar respuestas nulas del backend
 * sin romper el flujo de la tabla.
 */
export function buildEmptyPaginator(page: number, size: number): Paginator<any> {
  return {
    content: [],
    totalElements: 0,
    totalPages: 0,
    number: page,
    size: size,
    first: true,
    last: true,
    empty: true,
  };
}

