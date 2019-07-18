
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char start_codon[]="atg";
char stop_codon1[]="taa";
char stop_codon2[]="tag";
char stop_codon3[]="tga";

char t_aminoacid[16][2][10]={{"ttt","Phe[F]"},
                             {"ttc","Phe[F]"},
                             {"tta","Leu[L]"},
                             {"ttg","Leu[L]"},
                             {"tct","Ser[S]"},
                             {"tcc","Ser[S]"},
                             {"tca","Ser[S]"},
                             {"tcg","Ser[S]"},
                             {"tat","Tyr[Y]"},
                             {"tac","Tyr[Y]"},
                             {"taa","Ter[End]"},
                             {"tag","Ter[End]"},
                             {"tgt","Cys[C]"},
                             {"tgc","Cys[C]"},
                             {"tga","Ter[End]"},
                             {"tgg","Trp[W]"}};

char c_aminoacid[16][2][10]={{"ctt","Leu[L]"},
                             {"ctc","Leu[L]"},
                             {"cta","Leu[L]"},
                             {"ctg","Leu[L]"},
                             {"cct","Pro[P]"},
                             {"ccc","Pro[P]"},
                             {"cca","Pro[P]"},
                             {"ccg","Pro[P]"},
                             {"cat","His[H]"},
                             {"cac","His[H]"},
                             {"caa","Gln[Q]"},
                             {"cag","Gln[Q]"},
                             {"cgt","Arg[R]"},
                             {"cgc","Arg[R]"},
                             {"cga","Arg[R]"},
                             {"cgg","Arg[R]"}};

char a_aminoacid[16][2][10]={{"att","Ile[I]"},
                             {"atc","Ile[I]"},
                             {"ata","Ile[I]"},
                             {"atg","Met[M]"},
                             {"act","Thr[T]"},
                             {"acc","Thr[T]"},
                             {"aca","Thr[T]"},
                             {"acg","Thr[T]"},
                             {"aat","Asn[N]"},
                             {"aac","Asn[N]"},
                             {"aaa","Lys[K]"},
                             {"aag","Lys[K]"},
                             {"agt","Ser[S]"},
                             {"agc","Ser[S]"},
                             {"aga","Arg[R]"},
                             {"agg","Arg[R]"}};

char g_aminoacid[16][2][10]={{"gtt","Val[V]"},
                             {"gtc","Val[V]"},
                             {"gta","Val[V]"},
                             {"gtg","Val[V]"},
                             {"gct","Ala[A]"},
                             {"gcc","Ala[A]"},
                             {"gca","Ala[A]"},
                             {"gcg","Ala[A]"},
                             {"gat","Asp[D]"},
                             {"gac","Asp[D]"},
                             {"gaa","Glu[E]"},
                             {"gag","Glu[E]"},
                             {"ggt","Gly[G]"},
                             {"ggc","Gly[G]"},
                             {"gga","Gly[G]"},
                             {"ggg","Gly[G]"}};

int protein_count=0, amino_acid_count=0;

void print_amino_acid(char *amino_acid)
{
    int i;
    if (amino_acid[0]=='t')
        for (i=0;i<16;i++)
            if (strcmp(amino_acid, t_aminoacid[i][0])==0)
                printf("%s --> %s\n",amino_acid, t_aminoacid[i][1]);
    else if (amino_acid[0]=='c')
        for (i=0;i<16;i++)
            if (strcmp(amino_acid, c_aminoacid[i][0])==0)
                printf("%s --> %s\n",amino_acid, c_aminoacid[i][1]);
    else if (amino_acid[0]=='a')
        for (i=0;i<16;i++)
            if (strcmp(amino_acid, a_aminoacid[i][0])==0)
                printf("%s --> %s\n",amino_acid, a_aminoacid[i][1]);
    else if (amino_acid[0]=='g')
        for (i=0;i<16;i++)
            if (strcmp(amino_acid, g_aminoacid[i][0])==0)
                printf("%s --> %s\n",amino_acid, g_aminoacid[i][1]);
}

int is_start_codon(char *amino_acid)
{
    if (strcmp(amino_acid, start_codon)==0)
        return 1;
    return 0;
}

int is_stop_codon(char *amino_acid)
{
    if ((strcmp(amino_acid, stop_codon1)==0) ||
            (strcmp(amino_acid, stop_codon2)==0) ||
            (strcmp(amino_acid, stop_codon3)==0))
        return 1;
    return 0;
}

void find_protein(char *buffer)
{
    char amino_acid[4];
    int k = 0,i;
    int is_protein_started = 0;
    for (i=0;buffer[i]!='\0';i++)
    {
        if (k<3)
        {
            amino_acid[k++]=buffer[i];
        }
        else
        {
            amino_acid[k]='\0';
            k=0;
            if (!is_protein_started)
            {
                if (is_start_codon(amino_acid))
                {
                    is_protein_started = 1;
                    printf("%s\n",amino_acid);
                }
            }
            else
            {
                if(!is_stop_codon(amino_acid))
                {
                    //copy
                    amino_acid_count++;
                    print_amino_acid(amino_acid);
                    //printf("%s\n",amino_acid);
                }
                else
                {
                    amino_acid_count++;
                    print_amino_acid(amino_acid);
                    is_protein_started = 0;
                    protein_count++;
                    printf("%d protein completed\n\n",protein_count);
                }
            }
        }
    }
}

int main()
{
    FILE *fptr;
    char filename[]="KR559470.fasta", c;
    int file_length=0, i=0, j=0, buf_length=0;;
    char *buffer = NULL;
    char *rev_buffer = NULL;

    // Open file
    fptr = fopen(filename, "r");
    if (fptr == NULL)
    {
        printf("Cannot open file sequence.txt\n");
        exit(0);
    }

    // Read contents from file
    c = fgetc(fptr);
    while (c != EOF)
    {
        if (c=='a'||c=='t'||c=='g'||c=='c')
        {
            //printf ("%c", c);
            file_length++;
        }
        c = fgetc(fptr);
    }
    fseek(fptr, 0, SEEK_SET);
    buffer = (char*)malloc((file_length+2)*sizeof(char));
    rev_buffer = (char*)malloc((file_length+2)*sizeof(char));

    // Read contents from file to buffer
    c = fgetc(fptr);
    while (c != EOF)
    {
        if (c=='a'||c=='t'||c=='g'||c=='c')
            buffer[i++]=c;
        buffer[i]='\0';
        c = fgetc(fptr);
    }

    // Reverse the nucleoids
    for (i=strlen(buffer)-1;i>=0;i--)
    {
        //printf("%c",buffer[i]);
        rev_buffer[j++]=buffer[i];
    }
    rev_buffer[j]='\0';

    find_protein(rev_buffer);
    printf("Total %d nucleoid found\n",file_length);
    printf("Total %d amino acids found\n",amino_acid_count);
    printf("Total %d protein found\n",protein_count);

    fclose(fptr);
    free(buffer);
    free(rev_buffer);
    return 0;
}
