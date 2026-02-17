import { Injectable, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';

@Injectable({
    providedIn: 'root',
})
export class WindowService {

    constructor(
        @Inject(DOCUMENT) private document: Document
    ) { }

    getWindowHeight(): number {
        return this.document.defaultView?.innerHeight || 0;
    }
}