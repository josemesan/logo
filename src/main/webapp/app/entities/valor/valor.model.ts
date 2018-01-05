import { BaseEntity } from './../../shared';

export class Valor implements BaseEntity {
    constructor(
        public id?: number,
        public nombre?: string,
        public campo?: number,
    ) {
    }
}
