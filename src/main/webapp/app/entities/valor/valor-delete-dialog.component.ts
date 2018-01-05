import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Valor } from './valor.model';
import { ValorPopupService } from './valor-popup.service';
import { ValorService } from './valor.service';

@Component({
    selector: 'jhi-valor-delete-dialog',
    templateUrl: './valor-delete-dialog.component.html'
})
export class ValorDeleteDialogComponent {

    valor: Valor;

    constructor(
        private valorService: ValorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.valorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'valorListModification',
                content: 'Deleted an valor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-valor-delete-popup',
    template: ''
})
export class ValorDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private valorPopupService: ValorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.valorPopupService
                .open(ValorDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
