/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   FormatUtils.cpp
 * Author: marinap
 * 
 * Created on February 13, 2018, 10:56 AM
 */

#include "FormatUtils.hpp"

const std::string FormatUtils::NEW_ATOM_BLOCK = "NEWATOM";
const std::string FormatUtils::IAC_BLOCK = "IAC";
const std::string FormatUtils::COUPL = "COUPL";

FormatUtils::FormatUtils() {
}

FormatUtils::FormatUtils(const FormatUtils& orig) {
}

FormatUtils::~FormatUtils() {
}

void FormatUtils::readConf(char argv[], Config & conf) {
    
    std::ifstream inputFile;
    inputFile.open(argv);

    if (!inputFile.is_open()) {
        std::cerr << "ERROR in opening: " << argv << "\n";
        throw std::ios_base::failure("FormatUtils::readConf(char argv[], Config & conf)");
    }
    
    std::string line;
    while (std::getline(inputFile, line)) {
        
        std::vector<std::string> data;
        if (StringUtils::readLine(line, data, '#')) {
            
            try {
                if (data[Config::COLNAME] == "NSTEP") {
                    conf.setnStep(StringUtils::strToDouble(data[Config::COLVAL]));
                } else if (data[Config::COLNAME] == "TOL") {
                    conf.setTol(StringUtils::strToDouble(data[Config::COLVAL]));
                } else if (data[Config::COLNAME] == "STEPS") {
                    conf.setStepS(StringUtils::strToDouble(data[Config::COLVAL]));
                } else if (data[Config::COLNAME] == "STEPL") {
                    conf.setStepL(StringUtils::strToDouble(data[Config::COLVAL]));
                } else if (data[Config::COLNAME] == "GRADALLOWED") {
                    conf.setGradAllowed(StringUtils::strToDouble(data[Config::COLVAL]));
                } else if (data[Config::COLNAME] == "MAXSIZE") {
                    conf.setMaxSize(StringUtils::strToDouble(data[Config::COLVAL]));
                }
            } catch (const ConversionExcept &e) {
                std::cerr << "\nFormatUtils::readConf(char argv[], Config & conf)\n"
                          << "\tin line: " + line + "\n";
                throw e;
            }
        }
    }
}


void FormatUtils::readMatrix(char argv[], std::vector<AtomType>& atomTypeVec) {

    std::ifstream inputFile;
    inputFile.open(argv);

    if (!inputFile.is_open()) {
        std::cerr << "ERROR in opening: " << argv << "\n";
        throw std::ios_base::failure("FormatUtils::readMatrix(char argv[], std::vector<AtomType>& atomTypeVec)");
    }

    std::string line;
    while (std::getline(inputFile, line)) {

        std::vector<std::string> data;
        if (StringUtils::readLine(line, data, '#')) {

            if (data[DATA_COL] == NEW_ATOM_BLOCK) {
                atomTypeVec.push_back(AtomType());
                createMatrix(argv, inputFile, atomTypeVec);
            }
        }
    }

    // check matrix of every atomType
    int size = atomTypeVec.size();
    for (auto & atom: atomTypeVec) {
        if (atom.getMatrixSize() != size) {
            std::cerr << "Matrix for atom " << atom.getAtomName()
                      << " has wrong size\n";
            throw std::logic_error("FormatUtils::readMatrix(char argv[], std::vector<AtomType>& atomTypeVec)");
        }
    }
}


void FormatUtils::createMatrix(char argv[], std::ifstream& inputFile, std::vector<AtomType>& atomTypeVec) {

    // it is assumed that atomTypeVec has already pushed_back one object
    int idx = atomTypeVec.size() - 1;
    AtomType & atomType = atomTypeVec[idx];
    
    
    std::string line;
    while (std::getline(inputFile, line)) {

        std::vector<std::string> data;
        if (StringUtils::readLine(line, data, '#')) {
            
            try {

            
            if (data[DATA_COL] == IAC_BLOCK) {

                // idx starts from 0
                atomType.setIdx(StringUtils::strToInt(data[IDX_COL])-1);
                atomType.setName(data[ATM_COL]);
                atomType.setC06(StringUtils::strToDouble(data[C06_COL]));

                // is there is an extra field -> set this atom as constant
                if (data.size() == CONST_COLUMN + 1)
                    atomType.setConstant();

                std::vector<double> c12Vec;
                c12Vec.push_back(StringUtils::strToDouble(data[C12_1_COL]));
                c12Vec.push_back(StringUtils::strToDouble(data[C12_2_COL]));
                c12Vec.push_back(StringUtils::strToDouble(data[C12_3_COL]));

                atomType.setC12Vec(c12Vec);

            } else if (data[DATA_COL] == "NEI") {

                atomType.setNeiC06(StringUtils::strToDouble(data[C06_NB_COL]));
                atomType.setNeiC12(StringUtils::strToDouble(data[C12_NB_COL]));

            } else if (data[0] == "MATRIX") {

                std::vector<int> matrix;
                for (int i = 1; i < data.size(); i++) {
                    matrix.push_back(StringUtils::strToInt(data[i]));
                }
                atomType.setMatrix(matrix);

            } else if (data[DATA_COL] == "ENDATOM") {
                return;
            } else {
                //return false;
                std::cerr << "\nFormatUtils::createMatrix(char argv[], std::ifstream& inputFile, std::vector<AtomType>& atomTypeVec)\n";
                throw IOExcept(line, argv);
            }
            
            } catch (const ConversionExcept &e) {
                std::cerr << "\nFormatUtils::createMatrix(char argv[], std::ifstream& inputFile, std::vector<AtomType>& atomTypeVec)\n"
                          << "\tin line: <<" + line + ">>\n";
                throw e;
            }
        }
    }
}


void FormatUtils::readParam(char argv[],
    std::vector<Parameter> & prmVec,
    std::vector<Parameter*> & constrPrmVec,
    std::vector<AtomType> & atomTypeVec) {

    std::ifstream inputFile;
    inputFile.open(argv);

    if (!inputFile.is_open()) {
        std::cerr << "ERROR while opening: " << argv << '\n';
        throw std::ios_base::failure("FormatUtils::readParam()");
    }
    
    std::string line;
    while (std::getline(inputFile, line)) {
        
        std::vector<std::string> data;
        if (StringUtils::readLine(line,data, '#')) {
            // Check if input file has at least 6 columns
            // [name|val|dp0|scale|SYN|n]
            if (data.size() < 6) {
                std::cerr << "Wrong number of columns: \n"
                          << data.size() << " instead of at least 6 columns.\n";
                throw IOExcept(line, argv);
            }
            
            Parameter prm;
            // get name, value, dp, if const and scale for prm
            getInfo(argv, data, prm);
            
            // Read SYN column and add synonyms to prm
            readSyn(argv, data, prm);
            
            /*
             * Check if prm or its syn are in prmVec
             * If yes: add its syn to synVec of prm already in prmVec
             * If not: add prm to prmVec
             */
            if (!addToPrmVec(prm, prmVec)) {
                prm.setPrmVecIdx(prmVec.size());
                prmVec.push_back(prm);
                
                std::vector<std::string>::iterator dataIt;
                for (dataIt = data.begin(); dataIt != data.end(); dataIt++) {
                    if (*dataIt == "COUPL") {
                        break;
                    }
                }
                
                // TODO
                // Should I include constant prms?
                // ???
                
                // Add constraints
                if (dataIt != data.end()) {
                    // TODO
                    // I assume that the current prm was added to prmVec
                    // -> only use COUPL to prm that was specified for the 1st time
                    // is there a case when it is not added?
                    // -> if prm is a syn of a prm already in prmVec
                    // what happens?
                    // -> constrain is not considered
                    // save its related parameters into the constrVec
                    // What about the combination of -%- and COPUL
                    // -> throw an illegal exception
                    int prmPos = prmVec.size() - 1;
                    readCouple(argv, dataIt, prm, prmPos, prmVec, constrPrmVec, atomTypeVec);
                }
            }
        }
    }
}


// read name, value, dp, if const and scale
void FormatUtils::getInfo(char argv[],
        std::vector<std::string> & data,
        Parameter & prm) {

    double dp0;
    
    try {

        if (data[DPRMCOL] == "-%-") {
            double prmVal = StringUtils::strToDouble(data[VALPRMCOL]);
            prm = Parameter(data[NAMEPRMCOL], prmVal, true, NAN, NAN);

        } else if (dp0 = StringUtils::strToDouble(data[DPRMCOL])) {
            double prmVal = StringUtils::strToDouble(data[VALPRMCOL]);
            double scale = StringUtils::strToDouble(data[SCALECOL]);
            prm = Parameter(data[NAMEPRMCOL], prmVal, false, dp0, scale);

        } else {
            std::cerr << "FormatUtils::readDp()\n";
            throw IOExcept(data[DPRMCOL], argv);
        }
        
    } catch (const ConversionExcept &e) {
        std::cerr << "\nFormatUtils::readDp()\n";
        throw e;
    }
}


void FormatUtils::readSyn(char argv[],
        std::vector<std::string> & data,
        Parameter & prm) {

    // check is 4th column == SYN (defined in Parameter.hpp))
    if (data[SYNCOL] != "SYN") {
        std::cerr << SYNCOL << "th column of " << argv << " must be equal to 'SYN' \n";
        throw IOExcept(data[SYNCOL], argv);
    }

    // Add synonyms to prm
    try {
        double synNum = StringUtils::strToInt(data[NUMSYN]);
        
        prm.addSyn(data, synNum, SYN);
        //prm.print();
        
    } catch (const ConversionExcept &e) {
        std::cerr << "\nFormatUtils::readSyn()\n";
        throw e;
    }
}


bool FormatUtils::addToPrmVec(Parameter prm, std::vector<Parameter>& prmVec) {
    
    std::vector<Parameter>::iterator prmIt;
    for (prmIt = prmVec.begin(); prmIt != prmVec.end(); prmIt++) {
        
        // Check if prm is in prmVec
        if (prmIt->getName() == prm.getName()) {
            throw std::ios_base::failure("FormatUtils::addToPrmVec()\n\t"
                    + prmIt->getName() + " already exists in prmVec");
            
        } else {
            // Check if prm is in synVec of prmIt
            if (prmIt->hasSyn(prm.getName())) {
                // Add prm's synonyms to synVec of prmIt
                prmIt->addSyn(prm.getSynSet());
                return true;

            // Check if prmIt is in synVec of prm
            } else if (prm.hasSyn(prmIt->getName())) {

                // Add prm and its synonyms to synVec of prmIt
                prmIt->addSyn(prm.getName());
                prmIt->addSyn(prm.getSynSet());
                return true;

            } else {
                // Check if a syn of prm matches with a syn prmIt
                std::set<std::string>::iterator synIt1;
                std::set<std::string>::iterator synIt2;
                
                std::set<std::string> synSet1 = prmIt->getSynSet();
                std::set<std::string> synSet2 = prm.getSynSet();
                
                // Return true if prmIt or prm have any syn in common
                if (synSet1.size() != 0 && synSet2.size() != 0) {
                    
                    for (synIt1 = synSet1.begin(); synIt1 != synSet1.end(); synIt1++) {
                        for (synIt2 = synSet2.begin(); synIt2 != synSet2.end(); synIt2++) {

                            if (*synIt1 == *synIt2) {
                                prmIt->addSyn(prm.getName());
                                prmIt->addSyn(prm.getSynSet());

                                return true;
                            }
                        }
                    }
                }
            }
        }
    }
    // If at the end of prmVec return false        
    return false;
}


void FormatUtils::readCouple(char argv[],
    std::vector<std::string>::iterator& dataIt,
    Parameter& prm,
    int prmPos,
    std::vector<Parameter>& prmVec,
    std::vector<Parameter*> & constrPrmVec,
    std::vector<AtomType> & atomTypeVec) {
    
    // at this point prm should already be in prmVec and have an idx
    ++dataIt;
    
    
}
