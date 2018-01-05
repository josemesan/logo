import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ValorComponent } from './valor.component';
import { ValorDetailComponent } from './valor-detail.component';
import { ValorPopupComponent } from './valor-dialog.component';
import { ValorDeletePopupComponent } from './valor-delete-dialog.component';

export const valorRoute: Routes = [
    {
        path: 'valor',
        component: ValorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Valors'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'valor/:id',
        component: ValorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Valors'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const valorPopupRoute: Routes = [
    {
        path: 'valor-new',
        component: ValorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Valors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'valor/:id/edit',
        component: ValorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Valors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'valor/:id/delete',
        component: ValorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Valors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
