import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Valor } from './valor.model';
import { ValorService } from './valor.service';

@Component({
    selector: 'jhi-valor-detail',
    templateUrl: './valor-detail.component.html'
})
export class ValorDetailComponent implements OnInit, OnDestroy {

    valor: Valor;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private valorService: ValorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInValors();
    }

    load(id) {
        this.valorService.find(id).subscribe((valor) => {
            this.valor = valor;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInValors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'valorListModification',
            (response) => this.load(this.valor.id)
        );
    }
}
