	.section	__TEXT,__text,regular,pure_instructions
	.build_version macos, 26, 0	sdk_version 26, 1
	.globl	_main                           ; -- Begin function main
	.p2align	2
_main:                                  ; @main
	.cfi_startproc
; %bb.0:
	sub	sp, sp, #16
	.cfi_def_cfa_offset 16
	mov	w0, #0                          ; =0x0
	str	wzr, [sp, #12]
	adrp	x9, _count@PAGE
	ldr	w8, [x9, _count@PAGEOFF]
	add	w8, w8, #1
	str	w8, [x9, _count@PAGEOFF]
	add	sp, sp, #16
	ret
	.cfi_endproc
                                        ; -- End function
	.globl	_count                          ; @count
.zerofill __DATA,__common,_count,4,2
.subsections_via_symbols
