/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { LogoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ValorDetailComponent } from '../../../../../../main/webapp/app/entities/valor/valor-detail.component';
import { ValorService } from '../../../../../../main/webapp/app/entities/valor/valor.service';
import { Valor } from '../../../../../../main/webapp/app/entities/valor/valor.model';

describe('Component Tests', () => {

    describe('Valor Management Detail Component', () => {
        let comp: ValorDetailComponent;
        let fixture: ComponentFixture<ValorDetailComponent>;
        let service: ValorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LogoTestModule],
                declarations: [ValorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ValorService,
                    JhiEventManager
                ]
            }).overrideTemplate(ValorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ValorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ValorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Valor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.valor).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
