/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   FormatUtils.hpp
 * Author: marinap
 *
 * Created on February 13, 2018, 10:56 AM
 */

#ifndef FORMATUTILS_HPP
#define FORMATUTILS_HPP

#include "Config.hpp"
#include "IOExcept.hpp"

#include "AtomType.hpp"
#include "Parameter.hpp"

class FormatUtils {
public:
    // for param file
    static const int NAMEPRMCOL = 0;
    static const int VALPRMCOL  = 1;
    static const int DPRMCOL    = 2;
    static const int SCALECOL   = 3;
    static const int SYNCOL     = 4;
    static const int NUMSYN     = 5;
    static const int SYN        = 6;
    static const std::string COUPL;
    
    // for de_*.txt files
    static const int IDXPRMCOL    = 0;
    static const int NAMMATIXECOL = 1;
    static const int DERIVCOL     = 2;
    
    // for matrix file
    static const int DATA_COL = 0;
    static const int NEW_ATOM_COLUMN = 0;
    static const std::string NEW_ATOM_BLOCK;
    static const std::string IAC_BLOCK;
    static const int IDX_COL = 1;
    static const int ATM_COL = 2;
    static const int C06_COL = 3;
    static const int C12_1_COL = 4;
    static const int C12_2_COL = 5;
    static const int C12_3_COL = 6;
    static const int NEI_COL = 0;
    static const int C06_NB_COL = 1;
    static const int C12_NB_COL = 2;
    static const int CONST_COLUMN = 7;
    
    FormatUtils();
    FormatUtils(const FormatUtils& orig);
    virtual ~FormatUtils();
    
    // read configuration file
    static void readConf(char argv[], Config & conf);
    
    // read matrix file
    static void readMatrix(char argv[], std::vector<AtomType>& atomTypeVec);
    
    // create matrix for an atomType
    static void createMatrix(char argv[], std::ifstream& inputFile,
        std::vector<AtomType>& atomTypeVec);
    
    // read param file
    static void readParam(char argv[],
        std::vector<Parameter> & prmVec,
        std::vector<Parameter*> & constrPrmVec,
        std::vector<AtomType> & atomTypeVec);
    
    // read name, val, dp and scale of prm
    static void getInfo(char argv[],
        std::vector<std::string> & data,
        Parameter & prm);
    
    static void readSyn(char argv[],
        std::vector<std::string> & data,
        Parameter & prm);
    
    static bool addToPrmVec(Parameter prm, std::vector<Parameter> & prmVec);
    
    static void readCouple(char argv[],
        std::vector<std::string>::iterator & dataIt,
        Parameter & prm,
        int prmPos,
        std::vector<Parameter>& prmVec,
        std::vector<Parameter*> & constrPrmVec,
        std::vector<AtomType> & atomTypeVec);
};

#endif /* FORMATUTILS_HPP */

