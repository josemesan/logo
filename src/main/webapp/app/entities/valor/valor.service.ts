import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Valor } from './valor.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ValorService {

    private resourceUrl = SERVER_API_URL + 'api/valors';

    constructor(private http: Http) { }

    create(valor: Valor): Observable<Valor> {
        const copy = this.convert(valor);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(valor: Valor): Observable<Valor> {
        const copy = this.convert(valor);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Valor> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Valor.
     */
    private convertItemFromServer(json: any): Valor {
        const entity: Valor = Object.assign(new Valor(), json);
        return entity;
    }

    /**
     * Convert a Valor to a JSON which can be sent to the server.
     */
    private convert(valor: Valor): Valor {
        const copy: Valor = Object.assign({}, valor);
        return copy;
    }
}
