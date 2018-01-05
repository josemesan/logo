import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Valor } from './valor.model';
import { ValorPopupService } from './valor-popup.service';
import { ValorService } from './valor.service';

@Component({
    selector: 'jhi-valor-dialog',
    templateUrl: './valor-dialog.component.html'
})
export class ValorDialogComponent implements OnInit {

    valor: Valor;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private valorService: ValorService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.valor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.valorService.update(this.valor));
        } else {
            this.subscribeToSaveResponse(
                this.valorService.create(this.valor));
        }
    }

    private subscribeToSaveResponse(result: Observable<Valor>) {
        result.subscribe((res: Valor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Valor) {
        this.eventManager.broadcast({ name: 'valorListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-valor-popup',
    template: ''
})
export class ValorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private valorPopupService: ValorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.valorPopupService
                    .open(ValorDialogComponent as Component, params['id']);
            } else {
                this.valorPopupService
                    .open(ValorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
