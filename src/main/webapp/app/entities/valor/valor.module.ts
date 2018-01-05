import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LogoSharedModule } from '../../shared';
import {
    ValorService,
    ValorPopupService,
    ValorComponent,
    ValorDetailComponent,
    ValorDialogComponent,
    ValorPopupComponent,
    ValorDeletePopupComponent,
    ValorDeleteDialogComponent,
    valorRoute,
    valorPopupRoute,
} from './';

const ENTITY_STATES = [
    ...valorRoute,
    ...valorPopupRoute,
];

@NgModule({
    imports: [
        LogoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ValorComponent,
        ValorDetailComponent,
        ValorDialogComponent,
        ValorDeleteDialogComponent,
        ValorPopupComponent,
        ValorDeletePopupComponent,
    ],
    entryComponents: [
        ValorComponent,
        ValorDialogComponent,
        ValorPopupComponent,
        ValorDeleteDialogComponent,
        ValorDeletePopupComponent,
    ],
    providers: [
        ValorService,
        ValorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LogoValorModule {}
